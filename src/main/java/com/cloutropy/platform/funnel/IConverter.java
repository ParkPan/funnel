package com.cloutropy.platform.funnel;

import org.json.JSONObject;

import java.util.ArrayList;


public interface IConverter {
    ArrayList<Tuple<String,String>> convert(JSONObject parentNode, String key,String toKey, JSONContext ctx);
}
