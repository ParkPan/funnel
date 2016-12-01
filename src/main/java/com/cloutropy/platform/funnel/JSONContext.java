package com.cloutropy.platform.funnel;

import org.json.JSONObject;

import java.util.ArrayList;


public class JSONContext {
    public final JSONObject rootNode;
    public final ArrayList<JSONObject> uncleNodes;

    public JSONContext(JSONObject rootNode) {
        this.rootNode = rootNode;
        this.uncleNodes = new ArrayList<>();
    }
}