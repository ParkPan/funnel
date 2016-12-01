package com.cloutropy.platform.funnel;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;



@RestController
public class ExceptionReportController_v0 {

    private static Logger logger = CommonLogger.getLogger();
    private static String report_name = "client_exception_v0";

    @RequestMapping(value="/exception/peers/{peerid}",method = RequestMethod.POST)
    public String receiveExceptionReport(@RequestBody String body, HttpServletRequest request,
                                         @PathVariable String peerid) throws ControllerException {

        ErrorTuple error = ReportHandler.saveReport(0,report_name,logger,request,body,new Tuple<>("peer_id",peerid));
        if (error == null) {
            return "{}";
        }
        logger.error("op=[saveReport] cause=[{}] info=[{}] url=[{}]",error.second,body,String.format("/exception/peers/%s",peerid));
        throw new ControllerException(error);
    }
}
