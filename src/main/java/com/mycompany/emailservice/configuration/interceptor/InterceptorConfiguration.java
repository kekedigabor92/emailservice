package com.mycompany.emailservice.configuration.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final MdcHandlerInterceptor mdcHandlerInterceptor;

    public InterceptorConfiguration(MdcHandlerInterceptor mdcHandlerInterceptor) {
        this.mdcHandlerInterceptor = mdcHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcHandlerInterceptor);
    }
}
