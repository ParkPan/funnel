package com.cloutropy.platform.funnel;


import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;



@RestController
public class SDKReportController_v1 {
    private static final Logger logger = CommonLogger.getLogger();
    private static HashMap<String, String> tempUrlMaps = null;

    private String process(String url, HttpServletRequest request, String body,Tuple<String,String>... adds) throws  ControllerException {
        String report_name = URLMapper.getInstance().getReportName(url);
        if(report_name == null) {
            ErrorTuple err = new ErrorTuple(RestError.E_INTERNAL_ERROR,"missing report handler");
            throw new ControllerException(err);
        }
        ErrorTuple error = ReportHandler.saveReport(1,report_name,logger, request, body,adds);
        if (error == null) {
            return "{}";
        }
        logger.error("op=[saveReport] cause=[{}] info=[{}] url=[{}]",error.second,body,url);
        throw new ControllerException(error);
    }

    @RequestMapping(value={"/sdk/**", "/idc/**"}, method= RequestMethod.POST)
    public String receiveReportData(@RequestBody String body, HttpServletRequest request) throws ControllerException {
        String requestUrl = request.getRequestURI();
        if(tempUrlMaps == null) {
            tempUrlMaps = UrlMapsConfig.getInstance().getUrlMaps();
        }
        if(!tempUrlMaps.containsKey(requestUrl)) {
            ErrorTuple err = new ErrorTuple(RestError.E_URL_UNSUPPORT,"url not support");
            throw new ControllerException(err);
        }
        return process(requestUrl, request, body);
    }

//    @RequestMapping(value="/sdk/performance/vod/version/1", method= RequestMethod.POST)
//    public String receivePerformance(@RequestBody String body,
//                                     HttpServletRequest request) throws ControllerException {
//        return process("/sdk/performance/vod/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/exception/version/1",method = RequestMethod.POST)
//    public String receiveException(@RequestBody String body,
//                                   HttpServletRequest request) throws ControllerException {
//        return process("/sdk/exception/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/flow/upload/version/1",method = RequestMethod.POST)
//    public String receiveUploadFlow(@RequestBody String body,
//                                    HttpServletRequest request) throws ControllerException {
//        return process("/sdk/flow/upload/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/flow/download/version/1",method = RequestMethod.POST)
//    public String receiveDwonloadFlow(@RequestBody String body,
//                                      HttpServletRequest request) throws ControllerException {
//        return process("/sdk/flow/download/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/fod/version/1",method = RequestMethod.POST)
//    public String receiveFOD(@RequestBody String body,
//                             HttpServletRequest request) throws ControllerException {
////        //forward the message to the p2p message queue
////        if(!FodQueue.getInstance().addMessage(body)){
////            logger.error("op=[forwardFodToP2P] info=[{}]",body);
////        }
//
////        String remote_ip =request.getHeader("RemoteIp");
////        if(remote_ip == null){
////            remote_ip = request.getRemoteAddr();
////        }
////        return process("/sdk/fod/version/1",body,new Tuple<String, String>("public_ip",remote_ip));
//        return process("/sdk/fod/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/vv/version/1",method = RequestMethod.POST)
//    public String receiveVV(@RequestBody String body,
//                             HttpServletRequest request) throws ControllerException {
////        //forward the message to the p2p message queue
////        if(!FodQueue.getInstance().addMessage(body)){
////            logger.error("op=[forwardFodToP2P] info=[{}]",body);
////        }
//
////        String remote_ip =request.getHeader("RemoteIp");
////        if(remote_ip == null){
////            remote_ip = request.getRemoteAddr();
////        }
//        return process("/sdk/vv/version/1", request, body);
//    }
//
//    @RequestMapping(value="/idc/peer_connection_report",method = RequestMethod.POST)
//    public String receiveIDCConnections(@RequestBody String body,
//                                        HttpServletRequest request) throws  ControllerException {
//        return process("/idc/peer_connection_report", request, body);
//    }
//
//    @RequestMapping(value="/sdk/vf/version/1",method = RequestMethod.POST)
//    public String recieveVFFlow(@RequestBody String body,
//                                HttpServletRequest request) throws ControllerException {
//        return process("/sdk/vf/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/qos/version/1",method=RequestMethod.POST)
//    public String recieveQOS(@RequestBody String body,
//                             HttpServletRequest request) throws ControllerException {
//        return process("/sdk/qos/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/offering/version/1",method=RequestMethod.POST)
//    public String recievePeerOffering(@RequestBody String body,
//                                      HttpServletRequest request) throws ControllerException {
//        return process("/sdk/offering/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/push_state/version/1",method=RequestMethod.POST)
//    public String recievePushState(@RequestBody String body,
//                                   HttpServletRequest request) throws ControllerException {
//        return process("/sdk/push_state/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/live_delay/version/1",method=RequestMethod.POST)
//    public String recieveLiveDelay(@RequestBody String body,
//                                   HttpServletRequest request) throws ControllerException {
//        return process("/sdk/live_delay/version/1", request, body);
//    }
//
//    @RequestMapping(value = "/sdk/seed/v1",method=RequestMethod.POST)
//    public String recieveSeedInfo(@RequestBody String body,
//                                   HttpServletRequest request) throws ControllerException {
//        return process("/sdk/seed/v1", request, body);
//    }
}
