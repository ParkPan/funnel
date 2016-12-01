package com.cloutropy.platform.funnel;

import org.slf4j.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class EventSchema {

    public final String report_name;
    private final String topic_name;
    private  final  boolean skip_timestamp;
    private final EventSegment segment;

    private static Logger logger = CommonLogger.getLogger();

    public EventSchema(String report_name,String event_name,boolean skip_timestamp, EventSegment segment) {
        this.report_name = report_name;
        this.topic_name = event_name;
        this.skip_timestamp = skip_timestamp;
        this.segment = segment;
    }

    public ResultTuple<ArrayList<ArrayList<Tuple<String,String>>>> createEventFromJsonObject(int version,JSONObject obj,ArrayList<Tuple<String,String>> addition) {
        ArrayList<Tuple<String,String>> seg_list;
        if(addition == null) {
            seg_list = new ArrayList<>();
        }else{
            seg_list = (ArrayList<Tuple<String,String>>)addition.clone();
        }
        ArrayList<ArrayList<Tuple<String,String>>> split_array = null;
        //set topic name
        seg_list.add(0,new Tuple<>("topic",topic_name));
        // message id
        String id = null;
        ErrorTuple err_miss_solid_seg = new ErrorTuple(RestError.E_JSON_MISSING_KEY, "id/timestamp/time/begin");
        try {
            //set unique id
            if(obj.has("id")) {
                id = obj.getString("id");
//                seg_list.add(new Tuple<>("id",id));
            }else if(version > 0) {
                return new ResultTuple<>(err_miss_solid_seg, null);
            }
            if(!skip_timestamp) {
                // set timestamp
                long timestamp;
                timestamp = System.currentTimeMillis();
                seg_list.add(new Tuple<>("timestamp", timestamp + ""));
            }
            //record the input time to evaluate the performance for collecting
            seg_list.add(new Tuple<>("input_time",System.currentTimeMillis() + ""));

            JSONContext ctx = new JSONContext(obj);
            ResultTuple<TreeStringResult> ret = segment.parseNormal(obj, ctx);
            if(ret.first != null) {
                return new ResultTuple<>(ret.first,null);
            }
            split_array =  ret.second.serialize();
        } catch (Exception e) {
            ErrorTuple err = new ErrorTuple(RestError.E_UNKNOWN,e.getMessage());
            return new ResultTuple<>(err,null);
        }
        ArrayList<ArrayList<Tuple<String,String>>> arr = new ArrayList<>();
        int index = 0;
        for(ArrayList<Tuple<String,String>> one : split_array) {
            ArrayList<Tuple<String,String>> line = new ArrayList<>(seg_list);
            if(id != null){
                if(index > 0) {
                    line.add(new Tuple<>("id", id + "(" + index + ")"));
                } else {
                    line.add(new Tuple<>("id",id));
                }
            }
            line.addAll(one);
            arr.add(line);
            ++index;
        }
        return new ResultTuple<>(null,arr);
    }

    public static EventSchema create(String filepath){
        //读取json文件内容
        String jsonString = null;
        BufferedReader reader = null;
        try{
            FileInputStream fs = new FileInputStream(filepath);
            InputStreamReader inputStreamReader = new InputStreamReader(fs,"UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String content;
            StringBuilder sb = new StringBuilder();
            while((content=reader.readLine())!= null){
                sb.append(content);
            }
            jsonString = sb.toString();
        } catch (IOException e){
            return null;
        }finally {
            if(reader != null) {
                try
                {
                    reader.close();
                }catch (IOException e){

                }
            }
        }
        //解析
        try {
            JSONObject obj = new JSONObject(jsonString);
            String report_name = obj.getString("report");
            String topic_name = obj.getString("topic");
            boolean skip_timestamp = false;
            if(obj.has("skip_timestamp") && obj.getBoolean("skip_timestamp")) {
                skip_timestamp = true;
            }
            EventSegment seg = EventSegment.loadFromJsonObject(obj,true);
            return new EventSchema(report_name,topic_name,skip_timestamp,seg);
        } catch (Exception e) {
            logger.error("op=[load schema file] info=[file:{}] cause=[{}]",filepath,e.getMessage());
            return null;
        }
    }
}

