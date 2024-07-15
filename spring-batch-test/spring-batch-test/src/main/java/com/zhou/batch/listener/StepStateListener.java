package com.zhou.batch.listener;

import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepStateListener implements StepExecutionListener {
	private final Logger log;
	
	public StepStateListener(Logger log) {
		this.log = log;
	}
	
	public void beforeStep(StepExecution stepExecution) {
		log.info("Step({}) start state {}", stepExecution.getStepName(), stepExecution.getStatus());
	}
	
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("Step({}) end state {}", stepExecution.getStepName(), stepExecution.getStatus());
		return stepExecution.getExitStatus();
	}
}
