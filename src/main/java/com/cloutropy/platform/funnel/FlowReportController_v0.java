package com.cloutropy.platform.funnel;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
public class FlowReportController_v0 {
    private final Logger logger = CommonLogger.getLogger();
    private final String report_name = "flow_report_v0";

    @RequestMapping(value="/flow/peers/{peerid}",method = RequestMethod.POST)
    public String receiveVodReport(@RequestBody String body, HttpServletRequest request,
                                   @PathVariable String peerid) throws ControllerException {
        ErrorTuple error = ReportHandler.saveReport(0,report_name,logger,request,body,new Tuple<>("peer_id",peerid));
        if (error == null) {
            return "{}";
        }
        if(!error.second.equals("downloads")) {
            logger.error("op=[saveReport] cause=[{}] info=[{}] url=[{}]", error.second, body, String.format("/flow/peers/%s", peerid));
        }
        throw new ControllerException(error);
    }
}
