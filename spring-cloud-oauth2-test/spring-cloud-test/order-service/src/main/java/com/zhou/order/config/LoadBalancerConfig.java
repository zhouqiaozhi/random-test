package com.zhou.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
@LoadBalancerClient(value ="user-service", configuration = LoadBalancerConfig.class )
public class LoadBalancerConfig {
//	@Autowired
//	private NacosDiscoveryProperties nacosProps;
	
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	

}
