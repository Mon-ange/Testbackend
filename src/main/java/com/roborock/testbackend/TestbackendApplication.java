package com.roborock.testbackend;

import com.roborock.testbackend.services.storage.StorageProperties;
import com.roborock.testbackend.services.storage.StorageService;
import com.roborock.testbackend.services.testsuite.TestSuiteProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class, TestSuiteProperties.class})
public class TestbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestbackendApplication.class, args);
	}
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			//storageService.deleteAll();
			storageService.init();
		};
	}
}
