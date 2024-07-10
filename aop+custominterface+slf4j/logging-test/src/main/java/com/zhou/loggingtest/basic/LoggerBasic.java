package com.zhou.loggingtest.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerBasic {
    protected final Logger log;
    public LoggerBasic() {
        log = LoggerFactory.getLogger(this.getClass());
    }
}
