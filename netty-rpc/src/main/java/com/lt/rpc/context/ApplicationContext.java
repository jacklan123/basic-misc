package com.lt.rpc.context;

import com.alibaba.fastjson.JSONObject;
import com.lt.rpc.cilent.NettyClient;
import com.lt.rpc.common.RpcResponse;
import com.lt.rpc.config.ReferenceConfig;
import com.lt.rpc.config.ServiceConfig;
import com.lt.rpc.invoker.DefaultInvoker;
import com.lt.rpc.invoker.Invoker;
import com.lt.rpc.loadbalance.LoadBalancer;
import com.lt.rpc.loadbalance.RandomLoadbalancer;
import com.lt.rpc.registry.Registry;
import com.lt.rpc.registry.RegistryInfo;
import com.lt.rpc.registry.ZookeeperRegistry;
import com.lt.rpc.server.NettyServer;
import com.lt.rpc.util.InvokeUtils;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author lantian
 * @date 2019/09/29
 */
public class ApplicationContext {

    private List<ServiceConfig> serviceConfigs;

    private List<ReferenceConfig> referenceConfigs;

    private Registry registry;

    private NettyServer nettyServer;

    private ConcurrentLinkedQueue<RpcResponse> responses= new ConcurrentLinkedQueue();

    private Map<RegistryInfo, ChannelHandlerContext> channels = new ConcurrentHashMap<>();

    private Map<Class, List<RegistryInfo>> interfacesMethodRegistryList = new ConcurrentHashMap<>();

    private Map<String,Invoker> inProgressInvoker = new ConcurrentHashMap();

    private LoadBalancer loadBalancer = new RandomLoadbalancer();

    private Map<String, Method> interfaceMethods = new ConcurrentHashMap<>();


    private void initRegistry(String registryUrl) {
        if (registryUrl.startsWith("zookeeper://")) {
            registryUrl = registryUrl.substring(12);
            registry = new ZookeeperRegistry(registryUrl);
        }
    }

    public ApplicationContext(String registryUrl, List serviceConfigs,
        List referenceConfigs, int port) throws Exception {
        // step 1: 保存服务提供者和消费者
        this.serviceConfigs = serviceConfigs == null ? new ArrayList<>() : serviceConfigs;
        this.referenceConfigs = referenceConfigs == null ? new ArrayList<>() : referenceConfigs;

        // step 2: 实例化注册中心
        initRegistry(registryUrl);

        // step 3: 将接口注册到注册中心，从注册中心获取接口，初始化服务接口列表
        RegistryInfo registryInfo = null;
        InetAddress addr = InetAddress.getLocalHost();
        String hostname = addr.getHostName();
        String hostAddress = addr.getHostAddress();
        registryInfo = new RegistryInfo(hostname, hostAddress, port);
        doRegistry(registryInfo);


        // step 4：初始化Netty服务器，接受到请求，直接打到服务提供者的service方法中
        if (!this.serviceConfigs.isEmpty()) {
            // 需要暴露接口才暴露
            nettyServer = new NettyServer(this.serviceConfigs, interfaceMethods);
            nettyServer.init(port);
        }


        // step 5：启动处理响应的processor
        initProcessor();

    }


    private ResponseProcessor[] processors;

    /**
     * 线程池处理
     */
    private void initProcessor() {
        // 事实上，这里可以通过配置文件读取，启动多少个processor
        int num = 3;
        processors = new ResponseProcessor[num];
        for (int i = 0; i < 3; i++) {
            processors[i] = createProcessor(i);
        }
    }


    private ResponseProcessor createProcessor(int i){
        ResponseProcessor responseProcessor = new ResponseProcessor();
        responseProcessor.start();
        return responseProcessor;
    }




    /**
     * 处理响应的线程
     */
    private class ResponseProcessor extends Thread {
        @Override
        public void run() {
            System.out.println("启动响应处理线程：" + getName());
            while (true) {
                // 多个线程在这里获取响应，只有一个成功
                RpcResponse response = responses.poll();
                if (response == null) {
                    try {
                        synchronized (ApplicationContext.this) {
                            // 如果没有响应，先休眠
                            ApplicationContext.this.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("收到一个响应：" + response);
                    String interfaceMethodIdentify = response.getInterfaceMethodIdentify();
                    String requestId = response.getRequestId();
                    String key = interfaceMethodIdentify + "#" + requestId;
                    Invoker invoker = inProgressInvoker.remove(key);
                    invoker.setResult(response.getResult());
                }
            }
        }
    }

    /**
     * 注册信息   从注册表中拉取信息
     * @param registryInfo
     * @throws Exception
     */
    private void doRegistry(RegistryInfo registryInfo) throws Exception {
        for (ServiceConfig config : serviceConfigs) {
            Class type = config.getType();
            registry.register(type, registryInfo);
            Method[] declaredMethods = type.getDeclaredMethods();
            for (Method method : declaredMethods) {
                String identify = InvokeUtils.buildInterfaceMethodIdentify(type, method);
                interfaceMethods.put(identify, method);
            }
        }


        for (ReferenceConfig config : referenceConfigs) {
            List registryInfos = registry.fetchRegistry(config.getType());
            if (registryInfos != null) {
                interfacesMethodRegistryList.put(config.getType(), registryInfos);
                initChannel(registryInfos);
            }
        }
    }

    private void initChannel(List<RegistryInfo> registryInfos) throws InterruptedException {
        for (RegistryInfo info : registryInfos) {
            if (!channels.containsKey(info)) {
                System.out.println("开始建立连接：" + info.getIp() + ", " + info.getPort());
                NettyClient client = new NettyClient(info.getIp(), info.getPort());
                client.setMessageCallback(message -> {
                    // 这里收单服务端返回的消息，先压入队列
                    RpcResponse response = JSONObject.parseObject(message, RpcResponse.class);
                    responses.offer(response);
                    synchronized (ApplicationContext.this) {
                        ApplicationContext.this.notifyAll();
                    }
                });

                // 等待连接建立
                ChannelHandlerContext ctx = client.getCtx();
                channels.put(info, ctx);
            }
        }
    }



    /**
     * 负责生成requestId的类
     */
    private LongAdder requestIdWorker = new LongAdder();

    /**
     * 获取调用服务
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                if ("equals".equals(methodName) || "hashCode".equals(methodName)) {
                    throw new IllegalAccessException("不能访问" + methodName + "方法");
                }
                if ("toString".equals(methodName)) {
                    return clazz.getName() + "#" + methodName;
                }


                // step 1: 获取服务地址列表
                List registryInfos = interfacesMethodRegistryList.get(clazz);

                if (registryInfos == null) {
                    throw new RuntimeException("无法找到服务提供者");
                }

                // step 2： 负载均衡
                RegistryInfo registryInfo = loadBalancer.choose(registryInfos);


                ChannelHandlerContext ctx = channels.get(registryInfo);
                String identify = InvokeUtils.buildInterfaceMethodIdentify(clazz, method);
                String requestId;
                synchronized (ApplicationContext.this) {
                    requestIdWorker.increment();
                    requestId = String.valueOf(requestIdWorker.longValue());
                }
                Invoker invoker = new DefaultInvoker(method.getReturnType(), ctx, requestId, identify);
                inProgressInvoker.put(identify + "#" + requestId, invoker);
                return invoker.invoke(args);
            }
        });
    }



}
