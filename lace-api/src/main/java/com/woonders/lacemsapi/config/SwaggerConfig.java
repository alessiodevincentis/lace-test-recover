package com.woonders.lacemsapi.config;

import com.woonders.lacemscommon.config.beanconfig.BaseCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created by Emanuele on 13/05/2017.
 */
@Configuration
//https://github.com/springfox/springfox/issues/1538
//@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.woonders.lacemsapi.controller")).build()
                .apiInfo(metaData()).enable(!new BaseCondition().isProduction());
    }

    private ApiInfo metaData() {
        return new ApiInfo("Spring REST API", "Spring REST API for LACE", "1.0", "Terms of service",
                new Contact("Emanuele Papa", "https://www.lacems.com", "info@lacems.com"), null, null);
    }
}
