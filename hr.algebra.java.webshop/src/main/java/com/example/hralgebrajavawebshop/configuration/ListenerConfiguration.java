package com.example.hralgebrajavawebshop.configuration;

import com.example.hralgebrajavawebshop.listener.CustomSessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListenerConfiguration {

    @Bean
    public ServletListenerRegistrationBean<CustomSessionListener> customSessionListenerServletListenerRegistrationBean() {
        ServletListenerRegistrationBean<CustomSessionListener> listenerRegBean =
                new ServletListenerRegistrationBean<>();

        listenerRegBean.setListener(new CustomSessionListener());
        return listenerRegBean;
    }
}
