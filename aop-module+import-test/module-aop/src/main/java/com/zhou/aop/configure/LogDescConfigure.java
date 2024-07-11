package com.zhou.aop.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhou.aop.aspect.ControllerAspect;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class LogDescConfigure {
    @Bean
    @ConditionalOnMissingBean(name = "controllerAspect")
    ControllerAspect controllerAspect(HttpServletRequest request, ObjectMapper objectMapper) {
        return new ControllerAspect(request, objectMapper);
    }
}
