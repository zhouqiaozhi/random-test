package com.zhou.batch.service;

import java.util.Map;

import org.springframework.scheduling.annotation.Async;

public interface BatchService {
	@Async
	void start(String jobName, Map<String, Object> paramsMap);
	Boolean startSync(String jobName, Map<String, Object> paramsMap);
}
