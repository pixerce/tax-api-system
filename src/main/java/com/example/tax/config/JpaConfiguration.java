package com.example.tax.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@Configuration
@EnableJpaRepositories(basePackages = {"com.example.tax.adapter"})
@EntityScan(basePackages = {"com.example.tax.adapter"})
public class JpaConfiguration {
}
