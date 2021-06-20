package com.example.demo;

import com.example.config.TestConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
@EnableCaching
@EnableBatchProcessing
public class DemoApplication {

	public static void main(String[] args) {
		//ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
		SpringApplication.run(DemoApplication.class, args);
	}

}
