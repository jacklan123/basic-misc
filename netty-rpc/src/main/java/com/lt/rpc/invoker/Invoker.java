package com.lt.rpc.invoker;

/**
 * @author lantian
 * @date 2019/09/29
 */
public interface Invoker<T> {

    Object invoke(Object[] args);


    void setResult(String result);

}
