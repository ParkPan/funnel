package com.cloutropy.platform.funnel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Reportor {

    static Logger logger = LoggerFactory.getLogger(Reportor.class);
    static final byte separator = 0x1F;

//    static void report(HashMap<String,String> segs) {
//        StringBuilder builder = new StringBuilder();
//        int count = 0,total=segs.size();
//        for(Map.Entry<String,String> entry : segs.entrySet()) {
//            builder.append(String.format("%s=%s",entry.getKey(),entry.getValue()));
//            if(++count < total) {
//                builder.append(separator);
//            }
//        }
//        logger.info(builder.toString());
//    }

    static void report(ArrayList<Tuple<String,String>> segs) {
        StringBuilder builder = new StringBuilder();
        int count = 0,total=segs.size();
        for(Tuple<String,String> seg : segs) {
            builder.append(seg);
            if(++count < total) {
                builder.append((char)separator);
            }
        }
        logger.info(builder.toString());
    }


}
