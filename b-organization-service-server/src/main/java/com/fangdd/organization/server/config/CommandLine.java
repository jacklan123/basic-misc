package com.fangdd.organization.server.config;

import com.fangdd.organization.server.manager.BizManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author lantian
 * @date 2019/10/09
 */
@Component
public class CommandLine implements ApplicationRunner {


    @Autowired
    private BizManager bizManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("---->当前时间" + bizManager.getTime());
    }
}
