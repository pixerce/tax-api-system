package com.example.tax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.example.tax.adapter"})
@EntityScan(basePackages = {"com.example.tax.adapter"})
@SpringBootApplication
public class TaxApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaxApiApplication.class, args);
	}

}
