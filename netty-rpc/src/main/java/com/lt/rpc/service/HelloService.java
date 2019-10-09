package com.lt.rpc.service;

import com.lt.rpc.model.TestBean;

/**
 * @author lantian
 */
public interface HelloService {

    /**
     *
     * @param testBean
     * @return
     */
    String sayHello(TestBean testBean);
}