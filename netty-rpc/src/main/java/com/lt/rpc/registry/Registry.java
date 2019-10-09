package com.lt.rpc.registry;

import java.util.List;

public interface Registry {
    /**
     * 将生产者接口注册到注册中心
     *
     * @param clazz        类
     * @param registryInfo 本机的注册信息
     */
    void register(Class clazz, RegistryInfo registryInfo) throws Exception;


    /**
     * 为服务提供者抓取注册表
     *
     * @param clazz 类
     * @return 服务提供者所在的机器列表
     */
    List<RegistryInfo> fetchRegistry(Class clazz) throws Exception;

}