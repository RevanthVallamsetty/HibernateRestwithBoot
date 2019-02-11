package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.collect.ImmutableList;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class HibernateRestWithBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HibernateRestWithBootApplication.class, args);
	}
	
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    final CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(ImmutableList.of("*"));
	    configuration.setAllowedMethods(ImmutableList.of("HEAD",
	            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
	    configuration.setAllowCredentials(true);
	    configuration.setAllowedHeaders(ImmutableList.of("*"));
	    configuration.setExposedHeaders(ImmutableList.of("X-Auth-Token","Authorization","Access-Control-Allow-Origin","Access-Control-Allow-Credentials"));
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/app/users", configuration);
	    return source;
	}

}

