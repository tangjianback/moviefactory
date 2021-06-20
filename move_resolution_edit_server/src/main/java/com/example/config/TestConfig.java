package com.example.config;

import com.example.dao.DataDeal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TestConfig {
    @Bean
    public DataDeal transferService() {
        System.out.println("创建");
        return new DataDeal();
    }

}
