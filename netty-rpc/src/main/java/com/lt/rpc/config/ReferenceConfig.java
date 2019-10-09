package com.lt.rpc.config;

/**
 * @author lantian
 */
public class ReferenceConfig{

    private Class type;

    public ReferenceConfig(Class type) {
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}