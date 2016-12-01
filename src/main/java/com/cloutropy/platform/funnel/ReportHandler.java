package com.cloutropy.platform.funnel;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;


public class ReportHandler {

    static public ErrorTuple saveReport(int version,String report_name,Logger logger, HttpServletRequest request,
                                        String jsonstr, Tuple<String,String>... adds) {
        ResultTuple<ArrayList<ArrayList<Tuple<String,String>>>> ret;
        ArrayList<EventSchema> schemas = SchemaMgr.getInstance().getSchema(report_name);
        if(schemas == null) {
            logger.error("op=[getSchema] info[{}]",report_name);
            return new ErrorTuple(RestError.E_INTERNAL_ERROR,"failed to get schema");
        }
        ArrayList<Tuple<String,String>> addition = new ArrayList<>();
        String remote_ip =request.getHeader("RemoteIp");
        if(remote_ip == null){
            remote_ip = request.getRemoteAddr();
        }
        String sdkName = "unknown";
        String sdkVersion = "0.0.0";
        String userAgentInfo =request.getHeader("User-Agent");
        if(userAgentInfo != null && !userAgentInfo.isEmpty()){
            if(userAgentInfo.contains("/")) {
                sdkVersion = getSDKVersionString(userAgentInfo.split("/")[1], 2);
                if(sdkVersion != null)
                    sdkName = userAgentInfo.split("/")[0];
                else
                    sdkVersion = "0.0.0";
            }
        }
        addition.add(new Tuple<>("public_ip",remote_ip));
        addition.add(new Tuple<>("sdk_agent_name",sdkName));
        addition.add(new Tuple<>("sdk_agent_version",sdkVersion));
        if(adds.length > 0) {
            for (Tuple<String, String> t : adds) {
                addition.add(t);
            }
        }

        // parse json string to json object
        JSONObject root;
        try {
            root = new JSONObject(jsonstr);
        } catch(JSONException e) {
            return new ErrorTuple(RestError.E_JSON_ILLEGAL_FORMAT,e.getMessage());
        }

        for(EventSchema schema : schemas) {
            ret = schema.createEventFromJsonObject(version,root,addition);
            if(ret.first != null) {
                if(version == 0 || ret.first.first == RestError.E_CAN_IGNORE){
                    continue;
                }
                return ret.first;
            }
            for(ArrayList<Tuple<String,String>> line : ret.second) {
                Reportor.report(line);
            }
        }
        return null;
    }

    private static String getSDKVersionString(String sdkVersion, int versionFieldLenth) {
        String[] versions = sdkVersion.split("\\.");
        if(versions.length != 3)
            return null;
        String tmpZero;
        for(int i=0; i<versions.length ; i++) {
            tmpZero = "";
            if(versions[i].length() < versionFieldLenth) {
                for( int j = versionFieldLenth-versions[i].length(); j>0; j--) {
                    tmpZero = tmpZero.concat("0");
                }
            } else if(versions[i].length() > versionFieldLenth){
                return null;
            }
            versions[i] = tmpZero.concat(versions[i]);
        }
        return StringUtils.join(Arrays.asList(versions), ".");
    }

//    private static String getSDKVersionString(String sdkVersion, int versionFieldLenth) {
//        if(!sdkVersion.contains("."))
//            return null;
//        int strCount = 0;
//        String tmpChar, tmpStr="";
//        StringBuffer retValStr= new StringBuffer();
//        for(int i=0; i<sdkVersion.length(); i++) {
//            tmpChar = sdkVersion.substring(i, i+1);
//            if(tmpChar.equals(".") || i == sdkVersion.length()-1) {
//                if(i == sdkVersion.length()-1) {
//                    strCount++;
//                    if(strCount > versionFieldLenth)
//                        return null;
//                }
//                for( int j = versionFieldLenth-strCount; j>0; j--) {
//                    retValStr.append("0");
//                }
//                retValStr.append(tmpStr);
//                tmpStr = "";
//                retValStr.append(tmpChar);
//                strCount = 0;
//                continue;
//            }
//            if(!Character.isDigit(sdkVersion.charAt(i)))
//                return null;
//            strCount++;
//            if(strCount > versionFieldLenth)
//                return null;
//            tmpStr = tmpStr.concat(tmpChar);
//        }
//        return retValStr.toString();
//    }
}
