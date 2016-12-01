package com.cloutropy.platform.funnel;


public enum RestError {
    //未知错误
    E_UNKNOWN,
    //内部错误
    E_INTERNAL_ERROR,
    //参数无效
    E_INVALID_PARAM,
    //JSON格式错误
    E_JSON_ILLEGAL_FORMAT,
    //JSON字段缺失
    E_JSON_MISSING_KEY,
    //可忽略的错误
    E_CAN_IGNORE,
    //url目前不支持
    E_URL_UNSUPPORT;
}
