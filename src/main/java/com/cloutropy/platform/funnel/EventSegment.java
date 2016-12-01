package com.cloutropy.platform.funnel;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class EventSegment {
    public final boolean required;
    public final String segName;
    public final ArrayList<EventNodeAttribute> nodeList;
    public final ArrayList<EventSegment> subSegments;
    public final boolean split;
    public final boolean can_ignore;

    public EventSegment(String seg_name, ArrayList<EventNodeAttribute> node_list,ArrayList<EventSegment> segs,
                        boolean required,boolean ignore, boolean split) {
        this.segName = seg_name;
        this.nodeList = node_list;
        this.subSegments = segs;
        this.required = required;
        this.split = split;
        this.can_ignore = ignore;
    }

    public ResultTuple<ArrayList<TreeStringResult>>  parse(JSONObject parent,JSONContext ctx) {
        ArrayList<Tuple<String,String>> seg_list = new ArrayList<>();
        if(!parent.has(segName)) {
            if(required){
                ErrorTuple err = null;
                if(can_ignore){
                    err = new ErrorTuple(RestError.E_CAN_IGNORE,segName);
                }else{
                    err = new ErrorTuple(RestError.E_JSON_MISSING_KEY,segName);
                }
                return new ResultTuple<>(err,null);
            }
            return new ResultTuple<>(null,null);
        }
        if(split){
            JSONArray arrNode = parent.getJSONArray(segName);
            return parseSplit(arrNode,ctx);
        }
        JSONObject node = parent.getJSONObject(segName);
        ResultTuple<TreeStringResult> ret = parseNormal(node,ctx);
        if(ret.first != null){
            return new ResultTuple<>(ret.first,null);
        }
        ArrayList<TreeStringResult> r = null;
        if(ret.second != null) {
            r = new ArrayList<>();
            r.add(ret.second);
        }
        return new ResultTuple<>(null,r);
    }

    public ResultTuple<TreeStringResult> parseNormal(JSONObject node,JSONContext ctx) {
        TreeStringResult tree = new TreeStringResult();
        if(nodeList != null) {
            ArrayList<Tuple<String, String>> top_segs = new ArrayList<>();
            for (EventNodeAttribute attr : nodeList) {
                ResultTuple<ArrayList<Tuple<String, String>>> ret = attr.parse(node, ctx);
                if (ret.first != null) {
                    return new ResultTuple<>(ret.first, null);
                }
                if(ret.second != null) {
                    top_segs.addAll(ret.second);
                }
            }
            tree.topSegs = top_segs;
        }

        if(subSegments != null) {
            tree.subTreeResults = new ArrayList<>();
            for (EventSegment seg : subSegments) {
                ResultTuple<ArrayList<TreeStringResult>> ret = seg.parse(node, ctx);
                if (ret.first != null) {
                    return new ResultTuple<>(ret.first, null);
                }
                if(ret.second != null) {
                    tree.subTreeResults.add(ret.second);
                }
            }
        }
        return new ResultTuple<>(null,tree);
    }

    private ResultTuple<ArrayList<TreeStringResult>> parseSplit(JSONArray arrNode, JSONContext ctx) {
        ArrayList<TreeStringResult> result = new ArrayList<>();
        ctx.uncleNodes.clear();
        for(int i=0;i<arrNode.length();++i) {
            JSONObject me = arrNode.getJSONObject(i);
            ResultTuple<TreeStringResult> ret = parseNormal(me,ctx);
            if(ret.first != null) {
                return new ResultTuple<>(ret.first,null);
            }
            result.add(ret.second);
            ctx.uncleNodes.add(me);
        }
        return new ResultTuple<>(null,result);
    }

    public static EventSegment loadFromJsonObject(JSONObject segObj,boolean isRoot) {
        ArrayList<EventNodeAttribute> node_list = new ArrayList<>();
        ArrayList<EventSegment> sub_segments = new ArrayList<>();
        if(segObj.has("node_list")) {
            JSONArray arr = segObj.getJSONArray("node_list");
            for (int i=0;i<arr.length();++i) {
                JSONObject attrObj = arr.getJSONObject(i);
                EventNodeAttribute attr = EventNodeAttribute.loadFromJSONObject(attrObj);
                node_list.add(attr);
            }
        }
        if(segObj.has("segments")){
            JSONArray arr = segObj.getJSONArray("segments");
            for(int i=0;i<arr.length();++i) {
                JSONObject subSegObj = arr.getJSONObject(i);
                EventSegment seg = loadFromJsonObject(subSegObj,false);
                sub_segments.add(seg);
            }
        }

        String seg_name = null;
        if(!isRoot) {
           seg_name = segObj.getString("seg_name");
        }
        //是否必选项
        boolean required = true;
        if(segObj.has("required") && !segObj.getBoolean("required")) {
            required = false;
        }

        boolean can_ignore = false;
        if(segObj.has("can_ignore") && segObj.getBoolean("can_ignore")){
            can_ignore = true;
        }

        //是否为分裂数组
        boolean split = false;
        if(segObj.has("split") && segObj.getBoolean("split")) {
            split = true;
        }
        return new EventSegment(seg_name,node_list,sub_segments,required,can_ignore,split);
    }
}


