package com.woonders.lacemsjsf.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@ComponentScan({ "service", "com.woonders.lacemsjsf.view.preventivo", "com.woonders.lacemsjsf.view.coobbligato",
		"com.woonders.lacemsjsf.view.praticheincorso", "com.woonders.lacemsjsf.**", "com.woonders.lacemsjsf",
		"com.woonders.lacemsjsf.config", "com.woonders.lacemscommon.**" })
@EnableRetry
public class ApplicationConfig {

}
