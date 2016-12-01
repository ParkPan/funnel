package com.cloutropy.platform.funnel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;


public class UrlMapsConfig {

    private static boolean isInit = false;
    private static HashMap<String, String> confUrlMaps = null;
    private static UrlMapsConfig confInstance = new UrlMapsConfig();

    private final static Logger logger = LoggerFactory.getLogger(UrlMapsConfig.class);

    public static UrlMapsConfig getInstance() {
        return confInstance;
    }

    private UrlMapsConfig(){
    }

    public HashMap<String, String> getUrlMaps() {
        if(!isInit) {
            Properties pps = new Properties();
            InputStream in;
            try {
                in = new BufferedInputStream(new FileInputStream("./conf/urlmaps.properties"));
                pps.load(in);
            } catch (Exception e) {
                logger.error("op=[read url map config error, please check urlmaps property file exist.]");
                return null;
            }
            confUrlMaps = new HashMap<>();
            Enumeration en = pps.propertyNames();
            while (en.hasMoreElements()) {
                String strKey = (String) en.nextElement();
                String strValue = pps.getProperty(strKey);
                confUrlMaps.put(strKey, strValue);
            }
            isInit = true;
        }
        return confUrlMaps;
    }
}
