package com.lt.rpc.config;

/**
 * @author lantian
 * @param <T>
 */
public class ServiceConfig<T>{

    public Class type;

    public T instance;

    public ServiceConfig(Class type, T instance) {
        this.type = type;
        this.instance = instance;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }
}