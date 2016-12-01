package com.cloutropy.platform.funnel;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;



@RestController
public class SyncTimeController {

    @RequestMapping(value = "/sdk/sync_time",method = RequestMethod.GET)
    public String getServerTime() throws ControllerException {
        Date date = new Date();
        long timeTick = date.getTime();
        SimpleDateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return String.format("{\"timestamp\":%d,\n\"timestamp_ms\":%d,\n\"datetime\":\"%s\"}",
                timeTick/1000,timeTick,format.format(date));
    }
}
