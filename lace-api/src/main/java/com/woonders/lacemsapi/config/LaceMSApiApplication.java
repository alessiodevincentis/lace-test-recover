package com.woonders.lacemsapi.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.woonders.**", "com.woonders.lacemscommon.**" })
// extends SpringBootServletInitializer to create a classic war for Tomcat -
// https://spring.io/blog/2014/03/07/deploying-spring-boot-applications
public class LaceMSApiApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {
		SpringApplication.run(LaceMSApiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(LaceMSApiApplication.class);
	}

}
