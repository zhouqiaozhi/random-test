package com.zhou.batch.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhou.batch.basic.LoggerBasic;
import com.zhou.batch.service.BatchService;

@Service
public class SimpleBatchServiceImpl extends LoggerBasic implements BatchService {
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private List<Job> jobs;
	
	@Override
	public Boolean startSync(String jobName, Map<String, Object> paramsMap) {
		boolean result = true;
		for(var job: jobs) {
			if(jobName.equals(job.getName())) {
				try {
					var paramsBuilder = new JobParametersBuilder();
					for(var entry: paramsMap.entrySet()) {
						if(entry.getValue() instanceof String) {
							paramsBuilder.addString(entry.getKey(), (String)entry.getValue());
						} else {
							paramsBuilder.addLong(entry.getKey(), (Long) entry.getValue());
						}
						// ... other type
					}
					jobLauncher.run(job, paramsBuilder.toJobParameters());
				} catch (Exception e) {
					log.error(e.getMessage());
					result = false;
				}
			}
		}
		return result;
	}

	@Override
	public void start(String jobName, Map<String, Object> paramsMap) {
		startSync(jobName, paramsMap);
	}

}
