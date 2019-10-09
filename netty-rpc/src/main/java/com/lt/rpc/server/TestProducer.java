package com.lt.rpc.server;

import com.lt.rpc.config.ServiceConfig;
import com.lt.rpc.context.ApplicationContext;
import com.lt.rpc.service.HelloService;
import com.lt.rpc.service.impl.HelloServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lantian
 */
public class TestProducer {

    public static void main(String[] args) throws Exception {
        String connectionString = "zookeeper://localhost:2181";
        HelloService service = new HelloServiceImpl();
        ServiceConfig config = new ServiceConfig<>(HelloService.class, service);
        List serviceConfigList = new ArrayList<>();
        serviceConfigList.add(config);
        ApplicationContext ctx = new ApplicationContext(connectionString, serviceConfigList, null, 50071);
    }
}