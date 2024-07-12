package com.zhou.batch.listener;

import org.slf4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobStateListener implements JobExecutionListener {
	private final Logger log;
	
	public JobStateListener(Logger log) {
		this.log = log;
	}
	
	public void beforeJob(JobExecution jobExecution) {
		log.info("Job start state {}", jobExecution.getStatus());
	}
	
	public void afterJob(JobExecution jobExecution) {
		log.info("Job end state {}", jobExecution.getStatus());
	}
}
