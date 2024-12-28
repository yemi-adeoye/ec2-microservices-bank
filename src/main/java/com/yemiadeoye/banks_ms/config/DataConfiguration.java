package com.yemiadeoye.banks_ms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration()
public class DataConfiguration {
    @Autowired
    private Environment env;

}
