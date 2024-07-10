package com.zhou.aop.annotation;

import org.springframework.context.annotation.Import;

import com.zhou.aop.configure.LogDescConfigure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(LogDescConfigure.class)
public @interface EnableLogDesc {
}
