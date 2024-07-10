package com.zhou.loggingtest.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhou.loggingtest.annotation.MethodDesc;
import com.zhou.loggingtest.basic.LoggerBasic;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.stream.Collectors;

@Aspect
@Component
public class ControllerAspect extends LoggerBasic {

    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    ControllerAspect(HttpServletRequest request, ObjectMapper objectMapper) {
        this.request = request;
        this.objectMapper = objectMapper;
    }

    @Pointcut("execution(* com.zhou.loggingtest.controller.*.*(..)) && @annotation(com.zhou.loggingtest.annotation.MethodDesc)")
    private void desc() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void pointcutGet() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    private void pointcutPost() {}

    // KO(no annotation): curl --location 'http://localhost:8080/test'
    // OK(has annotation): curl --location 'http://localhost:8080/test/get/1?test=test'
    @Around("desc() && pointcutGet()")
    private Object aroundGet(ProceedingJoinPoint point) throws JsonProcessingException {
        long start = System.currentTimeMillis();
        var signature = (MethodSignature) point.getSignature();
        var method = signature.getMethod();
        var annotation = method.getAnnotation(MethodDesc.class);
        var methodName = Strings.isEmpty(annotation.v()) ? method.getName() : annotation.v();
        log.info("start call uri:{} name:{} queryParams: {}",
                request.getRequestURI(),
                methodName,
                objectMapper.writeValueAsString(request.getParameterMap())
        );
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        log.info("end call uri:{} name:{} use:{}ms response:{}",
                request.getRequestURI(),
                methodName,
                System.currentTimeMillis() - start,
                objectMapper.writeValueAsString(result)
        );
        return result;
    }

//     OK(has annotation): curl --location 'http://localhost:8080/test' \
//    --header 'Content-Type: application/json' \
//    --data '{
//        "test": "test"
//    }'
    @Around("desc() && pointcutPost()")
    private Object aroundPost(ProceedingJoinPoint point) throws IOException {
        long start = System.currentTimeMillis();
        var request = new ContentCachingRequestWrapper(this.request);
        var signature = (MethodSignature) point.getSignature();
        var method = signature.getMethod();
        var annotation = method.getAnnotation(MethodDesc.class);
        var methodName = Strings.isEmpty(annotation.v()) ? method.getName() : annotation.v();
        log.info("Start call uri:{} name:{} requestBody: {}",
                request.getRequestURI(),
                methodName,
                objectMapper.writeValueAsString(point.getArgs())
        );
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        log.info("End call uri:{} name:{} use:{}ms response:{}",
                request.getRequestURI(),
                methodName,
                System.currentTimeMillis() - start,
                objectMapper.writeValueAsString(result)
        );
        return result;
    }

    // OK(has annotation, not in [GET, POST]) curl --location --request PUT 'http://localhost:8080/test'
    @Around("desc() && !pointcutPost() && !pointcutPost()")
    private Object aroundOther(ProceedingJoinPoint point) {
        log.info("This is {}", request.getMethod());
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
