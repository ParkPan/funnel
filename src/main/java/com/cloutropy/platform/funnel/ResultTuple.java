package com.cloutropy.platform.funnel;


public class ResultTuple<T> extends Tuple<ErrorTuple,T> {

    public ResultTuple(ErrorTuple err,T result) {
        super(err,result);
    }
}

