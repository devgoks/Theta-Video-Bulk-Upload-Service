package com.theta.ThetaVideoApiBulkUpload.Service;

import com.theta.ThetaVideoApiBulkUpload.thetaRestClient.ThetaRestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ThetaVideoServiceMock {

    public String mockedResponse (){
        return "{\"status\":\"success\",\"message\":\"Operation Successful\",\"data\":[{\"id\":\"video_gy7e01nv71empt4ayi7chvn9v9\",\"duration\":\"30526\",\"state\":\"success\",\"progress\":100,\"error\":null,\"resolution\":null,\"metadata\":{},\"playback_uri\":\"https://media.thetavideoapi.com/video_gy7e01nv71empt4ayi7chvn9v9/master.m3u8\",\"player_uri\":\"https://player.thetavideoapi.com/video/video_gy7e01nv71empt4ayi7chvn9v9\",\"use_drm\":null,\"create_time\":\"2023-05-18T13:53:50.270Z\",\"update_time\":\"2023-05-18T13:54:32.812Z\",\"service_account_id\":\"srvacc_cc55yrv86dh8x5n2wygu4ipm1\",\"file_name\":null,\"sub_state\":\"none\",\"source_upload_id\":\"upload_3huf1smf3vebmu1g3s2vqxfgr\",\"source_uri\":null,\"playback_policy\":\"public\",\"start_time_override\":null},{\"id\":\"video_aryxi2jkqdktawaqwjwci2wjur\",\"duration\":\"30526\",\"state\":\"success\",\"progress\":100,\"error\":null,\"resolution\":null,\"metadata\":{},\"playback_uri\":\"https://media.thetavideoapi.com/video_aryxi2jkqdktawaqwjwci2wjur/master.m3u8\",\"player_uri\":\"https://player.thetavideoapi.com/video/video_aryxi2jkqdktawaqwjwci2wjur\",\"use_drm\":null,\"create_time\":\"2023-05-18T13:53:49.620Z\",\"update_time\":\"2023-05-18T13:54:29.283Z\",\"service_account_id\":\"srvacc_cc55yrv86dh8x5n2wygu4ipm1\",\"file_name\":null,\"sub_state\":\"none\",\"source_upload_id\":\"upload_b41w0hef4suvr9ytxshrfdn7m\",\"source_uri\":null,\"playback_policy\":\"public\",\"start_time_override\":null},{\"id\":\"video_j60p87jdsd8i5vyd12b4yrr74u\",\"duration\":\"30526\",\"state\":\"success\",\"progress\":100,\"error\":null,\"resolution\":null,\"metadata\":{},\"playback_uri\":\"https://media.thetavideoapi.com/video_j60p87jdsd8i5vyd12b4yrr74u/master.m3u8\",\"player_uri\":\"https://player.thetavideoapi.com/video/video_j60p87jdsd8i5vyd12b4yrr74u\",\"use_drm\":null,\"create_time\":\"2023-05-18T13:53:52.134Z\",\"update_time\":\"2023-05-18T13:54:34.698Z\",\"service_account_id\":\"srvacc_cc55yrv86dh8x5n2wygu4ipm1\",\"file_name\":null,\"sub_state\":\"none\",\"source_upload_id\":\"upload_7shrd2r22k72t8ggnrc6mitzx\",\"source_uri\":null,\"playback_policy\":\"public\",\"start_time_override\":null},{\"id\":\"video_4f79y37qjun8568mtgk61z6a2v\",\"duration\":\"30526\",\"state\":\"success\",\"progress\":100,\"error\":null,\"resolution\":null,\"metadata\":{},\"playback_uri\":\"https://media.thetavideoapi.com/video_4f79y37qjun8568mtgk61z6a2v/master.m3u8\",\"player_uri\":\"https://player.thetavideoapi.com/video/video_4f79y37qjun8568mtgk61z6a2v\",\"use_drm\":null,\"create_time\":\"2023-05-18T13:54:37.467Z\",\"update_time\":\"2023-05-18T13:54:48.945Z\",\"service_account_id\":\"srvacc_cc55yrv86dh8x5n2wygu4ipm1\",\"file_name\":null,\"sub_state\":\"none\",\"source_upload_id\":\"upload_52jagxk9e7n1fvssi17w08rv9\",\"source_uri\":null,\"playback_policy\":\"public\",\"start_time_override\":null},{\"id\":\"video_k6tmn4d0dn9fnrednjvydvxgvx\",\"duration\":\"30526\",\"state\":\"success\",\"progress\":100,\"error\":null,\"resolution\":null,\"metadata\":{},\"playback_uri\":\"https://media.thetavideoapi.com/video_k6tmn4d0dn9fnrednjvydvxgvx/master.m3u8\",\"player_uri\":\"https://player.thetavideoapi.com/video/video_k6tmn4d0dn9fnrednjvydvxgvx\",\"use_drm\":null,\"create_time\":\"2023-05-18T13:54:38.107Z\",\"update_time\":\"2023-05-18T13:54:52.508Z\",\"service_account_id\":\"srvacc_cc55yrv86dh8x5n2wygu4ipm1\",\"file_name\":null,\"sub_state\":\"none\",\"source_upload_id\":\"upload_g7ekebvhwtxqzf0yxu2npg1ue\",\"source_uri\":null,\"playback_policy\":\"public\",\"start_time_override\":null},{\"id\":\"video_3vufreqcc04e8n9divu38b8r11\",\"duration\":\"30526\",\"state\":\"success\",\"progress\":100,\"error\":null,\"resolution\":null,\"metadata\":{},\"playback_uri\":\"https://media.thetavideoapi.com/video_3vufreqcc04e8n9divu38b8r11/master.m3u8\",\"player_uri\":\"https://player.thetavideoapi.com/video/video_3vufreqcc04e8n9divu38b8r11\",\"use_drm\":null,\"create_time\":\"2023-05-18T13:54:47.117Z\",\"update_time\":\"2023-05-18T13:55:29.039Z\",\"service_account_id\":\"srvacc_cc55yrv86dh8x5n2wygu4ipm1\",\"file_name\":null,\"sub_state\":\"none\",\"source_upload_id\":\"upload_hfi6y4mq4ubxkj3anxqh21ttu\",\"source_uri\":null,\"playback_policy\":\"public\",\"start_time_override\":null}]}";
    }
    @Async
    public void mockedAsyncFlow(String webhookUrl) throws InterruptedException {
        Thread.sleep(30000);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ThetaRestClient.sendPostRequest(webhookUrl, headers,mockedResponse(),Void.class);
    }

    // Single upload mock methods
    public String mockedSingleResponse (){
        return "{\"status\":\"success\",\"message\":\"Operation Successful\",\"data\":[{\"id\":\"video_gy7e01nv71empt4ayi7chvn9v9\",\"duration\":\"30526\",\"state\":\"success\",\"progress\":100,\"error\":null,\"resolution\":null,\"metadata\":{},\"playback_uri\":\"https://media.thetavideoapi.com/video_gy7e01nv71empt4ayi7chvn9v9/master.m3u8\",\"player_uri\":\"https://player.thetavideoapi.com/video/video_gy7e01nv71empt4ayi7chvn9v9\",\"use_drm\":null,\"create_time\":\"2023-05-18T13:53:50.270Z\",\"update_time\":\"2023-05-18T13:54:32.812Z\",\"service_account_id\":\"srvacc_cc55yrv86dh8x5n2wygu4ipm1\",\"file_name\":null,\"sub_state\":\"none\",\"source_upload_id\":\"upload_3huf1smf3vebmu1g3s2vqxfgr\",\"source_uri\":null,\"playback_policy\":\"public\",\"start_time_override\":null}]}";
    }

    @Async
    public void mockedSingleAsyncFlow(String webhookUrl) throws InterruptedException {
        Thread.sleep(30000);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ThetaRestClient.sendPostRequest(webhookUrl, headers, mockedSingleResponse(), Void.class);
    }
}
