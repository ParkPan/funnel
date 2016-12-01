package com.cloutropy.platform.funnel;

//import java.util.HashMap;


public class URLMapper {
    private static URLMapper ourInstance = new URLMapper();
    public static URLMapper getInstance() {
        return ourInstance;
    }

//    private HashMap<String,String> urls;
    private URLMapper() {
//        String url_list[] = {
//                "/sdk/performance/vod/version/1",
//                "/sdk/exception/version/1",
//                "/sdk/flow/upload/version/1",
//                "/sdk/flow/download/version/1",
//                "/sdk/vf/version/1",
//                "/sdk/fod/version/1",
//                "/sdk/vv/version/1",
//                "/idc/peer_connection_report",
//                "/sdk/qos/version/1",
//                "/sdk/offering/version/1",
//                "/sdk/push_state/version/1",
//                "/sdk/live_delay/version/1",
//                "/sdk/seed/v1"
//        };
//        String report_names[] = {
//                "vod_performance_v1",
//                "client_exception_v1",
//                "upload_flow_v1",
//                "download_flow_v1",
//                "video_flow_v1",
//                "fod_report_v1",
//                "vv_report_v1",
//                "idc_connections",
//                "play_qos",
//                "peer_offering_v1",
//                "push_state_v1",
//                "live_delay_v1",
//                "seed_info_v1"
//        };
//        urls = new HashMap<>();
//        for(int i=0;i<url_list.length;++i) {
//            urls.put(url_list[i],report_names[i]);
//        }
    }

    public String getReportName(String url) {
//        return urls.get(url);
        return UrlMapsConfig.getInstance().getUrlMaps().get(url);
    }
}
