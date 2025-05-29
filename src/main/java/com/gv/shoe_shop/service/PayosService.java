package com.gv.shoe_shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import vn.payos.PayOS;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@Configuration
public class PayosService implements WebMvcConfigurer {

	@Value("${payos.client-id}")
	private String clientId;

	@Value("${payos.api-key}")
	private String apiKey;

	@Value("${payos.checksum-key}")
	private String checksumKey;

	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*")
				.allowedHeaders("*")
				.exposedHeaders("*")
				.allowCredentials(false)
				.maxAge(3600); // Max age of the CORS pre-flight request
	}

}
