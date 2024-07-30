package com.techwave.olol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.techwave.olol.login.config.SecurityProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecurityProperties.class)
public class OlolApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlolApplication.class, args);
	}

}