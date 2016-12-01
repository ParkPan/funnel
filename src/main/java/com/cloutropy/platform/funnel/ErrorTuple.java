package com.cloutropy.platform.funnel;

import java.util.HashMap;
import java.util.Map;


public class ErrorTuple extends Tuple<RestError,String> {

    public ErrorTuple(RestError err,String info){
        super(err,info);
    }

    public Map<String,String> toErrorObject(){
        Map<String,String> m = new HashMap<String,String>();
        m.put("error",this.first.toString());
        if(this.second != null) {
            m.put("err_info",this.second);
        }
        return m;
    }
}
