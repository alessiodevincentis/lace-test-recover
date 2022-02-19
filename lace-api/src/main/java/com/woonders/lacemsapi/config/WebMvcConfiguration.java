package com.woonders.lacemsapi.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.woonders.lacemsapi.converter.*;
import com.woonders.lacemsapi.converter.serializer.ResponseCodeSerializer;
import com.woonders.lacemsapi.interceptor.RequestLogInterceptor;
import com.woonders.lacemsapi.model.app.BaseMessage;
import com.woonders.lacemscommon.db.entityenum.Provenienza;

/**
 * Created by Emanuele on 07/11/2016.
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private RequestLogInterceptor requestLogInterceptor;

	@Override
	public void addFormatters(final FormatterRegistry registry) {
		super.addFormatters(registry);
		registry.addConverter(new LocalDateConverter());
		registry.addConverter(new StringToIntConverter());
		registry.addConverter(new StringToBigDecimalConverter());
		registry.addConverter(new ClienteImpiegoConverter());
		registry.addConverter(new MessageTypeConverter());
		registry.addConverter(new ProvenienzaConverter());
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		super.addInterceptors(registry);
		registry.addInterceptor(requestLogInterceptor);
	}

	// configurazione jackson su come serializzare/deserializzare alcune classi
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalDeserialization() {
		return jacksonObjectMapperBuilder -> {
			jacksonObjectMapperBuilder.deserializerByType(LocalDate.class, new LocalDateConverter());
			jacksonObjectMapperBuilder.serializerByType(BaseMessage.ResponseCode.class, new ResponseCodeSerializer());
			jacksonObjectMapperBuilder.deserializerByType(Provenienza.class, new ProvenienzaConverter());
		};
	}
}
