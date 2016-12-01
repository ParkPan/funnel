package com.cloutropy.platform.funnel;

import org.json.JSONObject;

import java.util.ArrayList;


public class EventNodeAttribute {
    public final String event_seg;
    public final String json_seg;
    public final boolean required;
    public final String converter;
    public final boolean can_ignore;

    public EventNodeAttribute(String event_seg,String json_seg, boolean required,boolean can_ignore,String converter) {
        this.event_seg = event_seg;
        this.json_seg = json_seg;
        this.required = required;
        this.converter = converter;
        this.can_ignore = can_ignore;
    }

    public ResultTuple<ArrayList<Tuple<String,String>>> parse(JSONObject parent,JSONContext ctx) {
        ArrayList<Tuple<String,String>> seg_list = null;
        try {
            if(!parent.has(json_seg)){
                if(required){
                    ErrorTuple err = null;
                    if(can_ignore){
                        err = new ErrorTuple(RestError.E_CAN_IGNORE,json_seg);
                    }else {
                        err = new ErrorTuple(RestError.E_JSON_MISSING_KEY, json_seg);
                    }
                    return new ResultTuple<>(err,null);
                }
                return new ResultTuple<>(null,null);
            }
            if(converter != null) {
                seg_list = Converter.convert(parent, json_seg, event_seg, converter,ctx);
                if (seg_list == null) {
                    ErrorTuple err = new ErrorTuple(RestError.E_INTERNAL_ERROR, "failed to convert the object through converter:" + converter);
                    return new ResultTuple<>(err, null);
                }
            }else {
                Object value = parent.get(json_seg);
                Tuple<String, String> pair = new Tuple<>(event_seg, value + "");
                seg_list = new ArrayList<>();
                seg_list.add(pair);
            }
        } catch (Exception e) {
            ErrorTuple err = new ErrorTuple(RestError.E_UNKNOWN,e.getMessage());
            return new ResultTuple<>(err,null);
        }
        return new ResultTuple<>(null,seg_list);
    }

    public static EventNodeAttribute loadFromJSONObject(JSONObject attrObj) {
        String event_seg = attrObj.getString("event_seg");
        String json_seg = event_seg;
        if(attrObj.has("json_seg")) {
            json_seg = attrObj.getString("json_seg");
        }
        boolean attr_required = true;
        if(attrObj.has("required") && !attrObj.getBoolean("required")) {
            attr_required = false;
        }
        boolean can_ignore = false;
        if(attrObj.has("can_ignore") && attrObj.getBoolean("can_ignore")){
            can_ignore = true;
        }
        String converter = null;
        if(attrObj.has("converter")) {
            converter = attrObj.getString("converter");
        }
        return new EventNodeAttribute(event_seg,json_seg,attr_required,can_ignore,converter);
    }
}
