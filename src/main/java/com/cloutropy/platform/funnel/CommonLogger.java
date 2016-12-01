package com.cloutropy.platform.funnel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommonLogger {
    public static Logger getLogger() {
        return LoggerFactory.getLogger("system_log");
    }
}
