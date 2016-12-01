package com.cloutropy.platform.funnel;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@RestController
public class RestErrorHandler {

    @ExceptionHandler(ControllerException.class)
    private Map<String,String> handleException(ControllerException e){
        ErrorTuple error = e.error;
        Map<String,String> m = new HashMap<String,String>();
        m.put("error",error.first.toString());
        if(error.second != null) {
            m.put("err_info",error.second);
        }
        return m;
    }
}
