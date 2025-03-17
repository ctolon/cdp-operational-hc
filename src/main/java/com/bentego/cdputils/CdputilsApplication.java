package com.bentego.cdputils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.bentego.cdputils", "com.cloudera.api.swagger"})

public class CdputilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CdputilsApplication.class, args);
	}

}
