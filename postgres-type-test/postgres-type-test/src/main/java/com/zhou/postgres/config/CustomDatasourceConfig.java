package com.zhou.postgres.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zhou.postgres.datasource.CustomDatasource;

@Configuration
public class CustomDatasourceConfig {
	@ConfigurationProperties(prefix = "com.zhou.datasource")
	@Bean
	@Primary
	DataSource dataSource() {
		return DataSourceBuilder
				.create()
				.type(CustomDatasource.class)
				.build();
	}
}
