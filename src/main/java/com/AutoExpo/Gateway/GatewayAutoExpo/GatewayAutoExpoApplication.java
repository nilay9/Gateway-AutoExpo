package com.AutoExpo.Gateway.GatewayAutoExpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient 	// It acts as a eureka client
@EnableZuulProxy		// Enable Zuul
public class GatewayAutoExpoApplication {
	
	@Bean
	@LoadBalanced
	public RestTemplate getrestTemplate() {
		
//		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//		httpComponentsClientHttpRequestFactory.setConnectTimeout(5000);
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayAutoExpoApplication.class, args);
	}

}
