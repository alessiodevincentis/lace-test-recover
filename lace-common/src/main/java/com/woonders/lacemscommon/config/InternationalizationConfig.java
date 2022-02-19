package com.woonders.lacemscommon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * Created by ema98 on 7/26/2017.
 */
@Configuration
public class InternationalizationConfig {

    //http://www.baeldung.com/spring-boot-internationalization
    //https://gist.github.com/jonikarppinen/0d600b0c82edce890310
    //https://stackoverflow.com/questions/24956584/spring-utf-8-message-resource-from-external-jar-issue
    @Bean
    public ItalyReloadableResourceBundleMessageSource italyReloadableResourceBundleMessageSource() {
        ItalyReloadableResourceBundleMessageSource italyReloadableResourceBundleMessageSource = new ItalyReloadableResourceBundleMessageSource();
        italyReloadableResourceBundleMessageSource.setBasename("messages");
        italyReloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        //it kind of initializes the messagesource otherwise it always throws message not found exception!!! :/
        italyReloadableResourceBundleMessageSource.getMessage("msg.sms.logsmsreceived");
        return italyReloadableResourceBundleMessageSource;
    }

    public class ItalyReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

        public String getMessage(String code) {
            return super.getMessage(code, null, Locale.ITALY);
        }

    }
}
