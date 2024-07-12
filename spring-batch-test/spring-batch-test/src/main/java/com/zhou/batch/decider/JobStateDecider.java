package com.zhou.batch.decider;

import java.util.Random;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class JobStateDecider implements JobExecutionDecider {
	private final Random random = new Random();
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		int result = random.nextInt(3);
		if(result == 2) {
			return new FlowExecutionStatus("KO");
		}
		return new FlowExecutionStatus("OK");
	}

}
