package com.cloutropy.platform.funnel;


public class ControllerException extends Exception{

    public ControllerException(ErrorTuple e){
        super();
        this.error = e;
    }
    public ErrorTuple error;
}
