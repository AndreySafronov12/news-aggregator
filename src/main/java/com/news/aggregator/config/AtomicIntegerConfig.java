package com.news.aggregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AtomicIntegerConfig {
    @Bean
    @Scope(value = "prototype")
    public AtomicInteger atomicInteger() {
        return new AtomicInteger(0);
    }
}
