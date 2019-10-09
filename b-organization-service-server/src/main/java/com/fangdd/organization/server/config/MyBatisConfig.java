/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.fangdd.organization.server.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis扫描接口，使用的tk.mybatis.spring.mapper.MapperScannerConfigurer，如果你不使用通用Mapper
 * 注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
 *
 * @author rocyuan
 */
@Configuration
@MapperScan("com.fangdd.organization.server.dao")
public class MyBatisConfig {


}
