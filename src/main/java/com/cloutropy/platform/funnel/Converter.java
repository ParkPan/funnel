package com.cloutropy.platform.funnel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Converter {

    private final static String INTEGER_ARRAY = "INTEGER_ARRAY";
    private final static String MAC_ADDRESS_LIST = "MAC_ADDRESS_LIST";
    private final static String ERROR_MODE_V0 = "ERROR_MODE_V0";
    private final static String ERROR_MSG_V0 = "ERROR_MSG_V0";
    private final static String TIMESTAMP_MILLISECOND = "TIMESTAMP_MILLISECOND";
    private final static String TIMESTAMP_CURRENT = "TIMESTAMP_CURRENT";

    private static HashMap<String,String> errMap;

    private static HashMap<String,IConverter> converters;

    public static void initialize(){
        converters = new HashMap<>();
        IConverter converter;
        converter = new IConverter() {
            @Override
            public ArrayList<Tuple<String, String>> convert(JSONObject parentNode, String key,String toKey,JSONContext ctx) {
                return convertIntegerArray(parentNode,key,toKey,ctx);
            }
        };
        converters.put(INTEGER_ARRAY,converter);

        converter = new IConverter() {
            @Override
            public ArrayList<Tuple<String, String>> convert(JSONObject parentNode, String key,String toKey,JSONContext ctx) {
                return convertMacAddrList(parentNode, key, toKey, ctx);
            }
        };
        converters.put(MAC_ADDRESS_LIST,converter);

        converter = new IConverter() {
            @Override
            public ArrayList<Tuple<String, String>> convert(JSONObject parentNode, String key,String toKey,JSONContext ctx) {
                return convertErrorMode_v0(parentNode, key, toKey, ctx);
            }
        };
        converters.put(ERROR_MODE_V0,converter);

        converter = new IConverter() {
            @Override
            public ArrayList<Tuple<String, String>> convert(JSONObject parentNode, String key,String toKey,JSONContext ctx) {
                return convertErrorMsg_v0(parentNode, key, toKey, ctx);
            }
        };
        converters.put(ERROR_MSG_V0,converter);

        converter = new IConverter() {
            @Override
            public ArrayList<Tuple<String, String>> convert(JSONObject parentNode, String key, String toKey, JSONContext context) {
                return convertTimestampToMillisecond(parentNode, key, toKey, context);
            }
        };
        converters.put(TIMESTAMP_MILLISECOND,converter);

        converter = new IConverter() {
            @Override
            public ArrayList<Tuple<String, String>> convert(JSONObject parentNode, String key, String toKey, JSONContext context) {
                return convertTimestampToCurrent(parentNode, key, toKey, context);
            }
        };
        converters.put(TIMESTAMP_CURRENT, converter);

        // just for compatible
        errMap = new HashMap<>();
        errMap.put("E_4XX_ERROR","E_4XX_ERROR");
        errMap.put("E_5XX_ERROR","E_5XX_ERROR");
        errMap.put("E_TIMEOUT","E_TIMEOUT");
        errMap.put("E_NOT_FOUND","E_NOT_FOUND");
    }

    public static ArrayList<Tuple<String,String>> convert(JSONObject parentNode,String key,String toKey,String convertName,JSONContext ctx) {
        IConverter converter = converters.get(convertName);
        if(converter == null) {
            return null;
        }
        return converter.convert(parentNode,key,toKey,ctx);
    }

    private static ArrayList<Tuple<String,String>> convertTimestampToMillisecond(JSONObject parentNode, String key, String toKey,JSONContext ctx) {
        long timestamp = parentNode.getLong(key)*1000;
        Tuple<String,String> ret = new Tuple<>(toKey,timestamp + "");
        ArrayList<Tuple<String,String>> arr = new ArrayList<>();
        arr.add(ret);
        return arr;
    }

    private static ArrayList<Tuple<String,String>> convertTimestampToCurrent(JSONObject parentNode, String key, String toKey,JSONContext ctx) {
        long timestamp = System.currentTimeMillis();  //parentNode.getLong(key)*1000;
        Tuple<String,String> ret = new Tuple<>(toKey,timestamp + "");
        ArrayList<Tuple<String,String>> arr = new ArrayList<>();
        arr.add(ret);
        return arr;
    }

    private static ArrayList<Tuple<String,String>> convertIntegerArray(JSONObject parentNode, String key,String toKey,JSONContext ctx) {
        JSONArray jsonArray = parentNode.getJSONArray(key);
        Tuple<String,String> ret = new Tuple<>(toKey,jsonArray.toString());
        ArrayList<Tuple<String,String>> arr = new ArrayList<>();
        arr.add(ret);
        return arr;
    }

    private static ArrayList<Tuple<String,String>> convertMacAddrList(JSONObject parentNode, String key, String toKey,JSONContext ctx) {
        ArrayList<Tuple<String,String>> arr = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        JSONArray jsonArray = parentNode.getJSONArray(key);
        for(int i=0;i< jsonArray.length();++i) {
            if(i > 0){
                builder.append(",");
            }
            builder.append(jsonArray.getJSONObject(i).getString("addr"));
        }
        arr.add(new Tuple<>("macs",builder.toString()));
        return arr;
    }

    private static ArrayList<Tuple<String,String>> convertErrorMode_v0(JSONObject parentNode, String key, String toKey,JSONContext ctx) {
        ArrayList<Tuple<String,String>>  arr = new ArrayList<>();
        String mode = parentNode.getString("mode");
        if(mode.equals("push")){
            arr.add(new Tuple<>("type","p2p"));
            arr.add(new Tuple<>("op","OP_PUSH_FILES"));
        }else if(mode.equals("start channel")) {
            arr.add(new Tuple<>("type","p2p"));
            arr.add(new Tuple<>("op","OP_START_CHANNEL"));
        }
        return arr;
    }

    private static ArrayList<Tuple<String,String>> convertErrorMsg_v0(JSONObject parentNode, String key, String toKey,JSONContext ctx) {
        ArrayList<Tuple<String,String>> arr = new ArrayList<>();
        String msg = parentNode.getString(key);
        String new_msg = errMap.get(msg);
        if(new_msg != null) {
            arr.add(new Tuple<>("error_code",new_msg));
        }else{
            arr.add(new Tuple<>("error_code","E_UNKNOWN_ERROR"));
        }
        return arr;
    }
}
