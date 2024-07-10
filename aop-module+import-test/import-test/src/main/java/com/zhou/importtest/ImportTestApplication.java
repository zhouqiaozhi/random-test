package com.zhou.importtest;

//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zhou.aop.annotation.EnableLogDesc;
//import com.zhou.aop.basic.LoggerBasic;

@SpringBootApplication
@EnableLogDesc
public class ImportTestApplication {
//	extends LoggerBasic implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(ImportTestApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		log.info("test");
//	}
	
}
