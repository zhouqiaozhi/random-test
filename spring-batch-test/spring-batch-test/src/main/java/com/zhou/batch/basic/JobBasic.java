package com.zhou.batch.basic;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class JobBasic extends LoggerBasic {
	protected String name;
	@Autowired
	protected JobRepository jobRepository;
	@Autowired
	protected PlatformTransactionManager platformTransactionManager;
	
	protected JobBasic() {
	}
	
	protected JobBasic(String name) {
		super(name);
		this.name = name;
	}
	
	protected abstract Job job();
}
