package com.ponscio_studio.n8n;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class N8nApplication {

	public static void main(String[] args) {
		SpringApplication.run(N8nApplication.class, args);
	}


	@Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://127.0.0.1:5500","http://localhost",
                "http://localhost:8080").allowedMethods("*").allowedHeaders("*").allowedOriginPatterns("*");
            }
        };
    }
}
