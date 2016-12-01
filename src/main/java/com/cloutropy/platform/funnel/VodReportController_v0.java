package com.cloutropy.platform.funnel;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
public class VodReportController_v0 {

    private static Logger logger = CommonLogger.getLogger();
    private static String report_name = "vod_performance_v0";

    @RequestMapping(value="/vod/peers/{peerid}/files/{fileid}",method = RequestMethod.POST)
    public String receiveVodReport(@RequestBody String body, HttpServletRequest request,
                                   @PathVariable String peerid,
                                   @PathVariable String fileid) throws ControllerException {
        ErrorTuple error = ReportHandler.saveReport(0,report_name,logger,request,body,new Tuple<>("peer_id",peerid),
                new Tuple<>("file_id",fileid));
        if (error == null) {
            return "{}";
        }
        logger.error("op=[saveReport] cause=[{}] info=[{}] url=[{}]",error.second,body,String.format("/vod/peers/%s/files/%s",peerid,fileid));
        throw new ControllerException(error);
    }

}





