package com.zhou.aop.aspect;

import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhou.aop.annotation.MethodDesc;
import com.zhou.aop.basic.LoggerBasic;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

@Aspect
public class ControllerAspect extends LoggerBasic {

    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    public ControllerAspect(HttpServletRequest request, ObjectMapper objectMapper) {
        this.request = request;
        this.objectMapper = objectMapper;
    }

    @Pointcut("execution(* com.zhou.*.controller.*.*(..)) && @annotation(com.zhou.aop.annotation.MethodDesc)")
    private void desc() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void pointcutGet() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    private void pointcutPost() {}

    // KO(no annotation): curl --location 'http://localhost:8080/test'
    // OK(has annotation): curl --location 'http://localhost:8080/test/get/1?test=test'
    @Around("desc() && pointcutGet()")
    private Object aroundGet(ProceedingJoinPoint point) throws JsonProcessingException {
        return requestResponseLog(point, (p) -> new AbstractMap.SimpleEntry<>("requestParams", request.getParameterMap()));
    }

//     OK(has annotation): curl --location 'http://localhost:8080/test' \
//    --header 'Content-Type: application/json' \
//    --data '{
//        "test": "test"
//    }'
    @Around("desc() && pointcutPost()")
    private Object aroundPost(ProceedingJoinPoint point) throws IOException {
        return requestResponseLog(point, (p) -> new AbstractMap.SimpleEntry<>("requestBody", p.getArgs()));
    }

    // OK(has annotation, not in [GET, POST]) curl --location --request PUT 'http://localhost:8080/test'
    @Around("desc() && !pointcutGet() && !pointcutPost()")
    private Object aroundOther(ProceedingJoinPoint point) throws JsonProcessingException {
        log.info("This is {}", request.getMethod());
        return next(point);
//    	return requestResponseLog(point, (p) -> null);
    }

    private Object requestResponseLog(ProceedingJoinPoint point, Function<ProceedingJoinPoint, Map.Entry<String, Object>> f) throws JsonProcessingException {
        var start = System.currentTimeMillis();
        var signature = (MethodSignature) point.getSignature();
        var method = signature.getMethod();
        var annotation = method.getAnnotation(MethodDesc.class);
        var methodName = Strings.isEmpty(annotation.v()) ? method.getName() : annotation.v();
        var requestInfo = f.apply(point);
        String requestLogText = "Start call uri:{} name:{} {}: {}";
        if(requestInfo == null) {
        	log.warn("requestInfo not found, please check a method");
        	requestLogText = requestLogText.substring(0, requestLogText.length() - 6);
        	requestInfo = new AbstractMap.SimpleEntry<>(null, null);
        }
        log.info(requestLogText,
                request.getRequestURI(),
                methodName,
                requestInfo.getKey(),
                objectMapper.writeValueAsString(requestInfo.getValue())
        );
        Object result = next(point);
        log.info("End call uri:{} name:{} use:{}ms response:{}",
                request.getRequestURI(),
                methodName,
                System.currentTimeMillis() - start,
                objectMapper.writeValueAsString(result)
        );
        return result;
    }

    private Object next(ProceedingJoinPoint point) {
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
