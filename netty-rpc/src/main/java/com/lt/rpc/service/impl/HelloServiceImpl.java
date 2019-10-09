package com.lt.rpc.service.impl;

import com.lt.rpc.model.TestBean;
import com.lt.rpc.service.HelloService;

/**
 * @author lantian
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(TestBean testBean) {
        return "牛逼,我收到了消息：" + testBean;
    }
}