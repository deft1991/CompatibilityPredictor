package com.deft.predict.exception;

import com.fasterxml.jackson.databind.DeserializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * @author Sergey Golitsyn
 * created on 03.02.2023
 */
@Configuration
@RequiredArgsConstructor
public class Config implements InitializingBean {

    private final RequestMappingHandlerAdapter converter;

    @Override
    public void afterPropertiesSet() {
        configureJacksonToFailOnUnknownProperties();
    }

    private void configureJacksonToFailOnUnknownProperties() {
        MappingJackson2HttpMessageConverter httpMessageConverter = converter.getMessageConverters().stream()
                .filter(mc -> mc.getClass()
                        .equals(MappingJackson2HttpMessageConverter.class))
                .map(mc -> (MappingJackson2HttpMessageConverter) mc)
                .findFirst()
                .get();

        httpMessageConverter.getObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
