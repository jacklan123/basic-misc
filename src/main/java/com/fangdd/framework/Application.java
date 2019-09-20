package com.fangdd.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lantian
 * @date 2019/08/30
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.fangdd")
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class);

    }

}
