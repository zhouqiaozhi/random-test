package com.zhou.batch.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerBasic {
	protected final Logger log;
	
	protected LoggerBasic() {
		log = LoggerFactory.getLogger(this.getClass());
	}
	
	protected LoggerBasic(String name) {
		log = LoggerFactory.getLogger(name);
	}
}
