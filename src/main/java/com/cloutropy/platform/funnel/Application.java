package com.cloutropy.platform.funnel;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.log4j.BasicConfigurator;

//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.config.java.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//
//import java.util.HashMap;
//import java.util.Map;

//@Configuration
//@ConfigurationProperties(prefix = "urlmap")
//@PropertySource("./conf:urlmaps.properties")
//class urlmapsConfig {
//    public static Map<String, String> url_report = new HashMap<>();
//    public Map<String, String> getUrl_report() {
//        return  url_report;
//    }
//    public void setUrl_report(Map<String, String> tmp_map) {
//        url_report = tmp_map;
//    }
//}

@SpringBootApplication
public class Application {
    public Application() {
        int a = 100;
    }

    private static Logger logger = CommonLogger.getLogger();

    public static void main(String[] args) {
        //initialize the logger system
        BasicConfigurator.configure();
        PropertyConfigurator.configure("./conf/log4j.properties");

        //initialize the url maps
        if(UrlMapsConfig.getInstance().getUrlMaps() == null) {
            logger.error("op=[initializeUrlMapsConfig] info=[]", "read url map config property file error");
            return;
        }
        if(UrlMapsConfig.getInstance().getUrlMaps().isEmpty()) {
            logger.info("op=[cannot get any key-value maps from url map property file.]");
        }

        //initialize schema
        String schema_dir = "./conf/report_schema";
        if (!SchemaMgr.getInstance().load(schema_dir)) {
            logger.error("op=[initializeSchemaMgr] info=[]", schema_dir);
            return;
        }
        Converter.initialize();
        if (!FodQueue.getInstance().initialize()) {
            logger.info("failed to initailzie FodQueue");
            return;
        }

        SpringApplication.run(Application.class, args);
        logger.info("op=[finishStartup]");
    }
}
