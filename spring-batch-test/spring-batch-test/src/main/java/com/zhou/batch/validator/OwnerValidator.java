package com.zhou.batch.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class OwnerValidator implements JobParametersValidator {

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		var owner = parameters.getString("owner");
		if(owner == null || "".equals(owner)) {
			throw new JobParametersInvalidException("owner not found");
		}
	}

}
