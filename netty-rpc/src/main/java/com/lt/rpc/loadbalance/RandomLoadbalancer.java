package com.lt.rpc.loadbalance;

import com.lt.rpc.registry.RegistryInfo;

import java.util.List;
import java.util.Random;

public class RandomLoadbalancer implements LoadBalancer {
    @Override
    public RegistryInfo choose(List<RegistryInfo> registryInfos) {
        Random random = new Random();
        int index = random.nextInt(registryInfos.size());
        return registryInfos.get(index);
    }

}