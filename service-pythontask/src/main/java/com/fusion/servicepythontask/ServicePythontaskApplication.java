package com.fusion.servicepythontask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ServicePythontaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicePythontaskApplication.class, args);
	}
}
