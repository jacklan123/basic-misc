package com.lt.rpc;

import com.lt.rpc.config.ReferenceConfig;
import com.lt.rpc.context.ApplicationContext;
import com.lt.rpc.model.TestBean;
import com.lt.rpc.service.HelloService;

import java.util.Collections;

/**
 * @author lantian
 */
public class TestConsumer {

    public static void main(String[] args) throws Exception {
        String connectionString = "zookeeper://localhost:2181";
        ReferenceConfig config = new ReferenceConfig(HelloService.class);
        ApplicationContext ctx = new ApplicationContext(connectionString, null, Collections.singletonList(config),
                50070);
        HelloService helloService = ctx.getService(HelloService.class);
        System.out.println("sayHello(TestBean)结果为：" + helloService.sayHello(new TestBean("张三", 20)));
    }
}