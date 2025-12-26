package com.example.tax.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class BeanConfiguration {

    @Bean("collection-executor")
    public Executor taskExecutor() {
        return Executors.newFixedThreadPool(1);
    }
}
