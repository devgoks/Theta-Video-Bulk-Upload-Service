package com.theta.ThetaVideoApiBulkUpload.Service;

import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.ProcessedVideoResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.Video;
import com.theta.ThetaVideoApiBulkUpload.thetaRestClient.ThetaRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class UploadResultAggregator {

    private final Map<String, List<Video>> batchIdToVideos = new ConcurrentHashMap<>();
    private final Map<String, Integer> batchIdToExpectedCount = new ConcurrentHashMap<>();
    private final Map<String, String> batchIdToWebhook = new ConcurrentHashMap<>();

    public void initBatch(String batchId, int expectedCount, String webhookUrl) {
        batchIdToExpectedCount.put(batchId, expectedCount);
        batchIdToWebhook.put(batchId, webhookUrl);
        batchIdToVideos.put(batchId, new ArrayList<>());
    }

    public void recordResult(String batchId, CheckVideoUploadResponse response, String webhookUrl) {
        batchIdToWebhook.putIfAbsent(batchId, webhookUrl);
        batchIdToVideos.computeIfAbsent(batchId, k -> new ArrayList<>())
                .add(response.getBody().getVideos().get(0));

        int expected = batchIdToExpectedCount.getOrDefault(batchId, 0);
        int current = batchIdToVideos.get(batchId).size();
        if (expected > 0 && current >= expected) {
            sendWebhook(batchId);
        }
    }

    private void sendWebhook(String batchId) {
        List<Video> videos = batchIdToVideos.getOrDefault(batchId, List.of());
        String webhook = batchIdToWebhook.get(batchId);
        var processed = new ProcessedVideoResponse();
        processed.setStatus("success");
        processed.setMessage("Operation Successful");
        processed.setVideos(videos);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ThetaRestClient.sendPostRequest(webhook, headers, processed, Void.class);

        cleanup(batchId);
        log.info("Posted final webhook for batch {} with {} videos", batchId, videos.size());
    }

    private void cleanup(String batchId) {
        batchIdToVideos.remove(batchId);
        batchIdToWebhook.remove(batchId);
        batchIdToExpectedCount.remove(batchId);
    }
}


