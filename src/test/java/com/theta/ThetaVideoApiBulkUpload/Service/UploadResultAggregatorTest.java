package com.theta.ThetaVideoApiBulkUpload.Service;

import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.Video;
import com.theta.ThetaVideoApiBulkUpload.thetaRestClient.ThetaRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class UploadResultAggregatorTest {

    private UploadResultAggregator aggregator;

    @BeforeEach
    void setUp() {
        aggregator = new UploadResultAggregator();
    }

    @Test
    void sendsWebhookWhenExpectedCountReached() {
        String batchId = "batch-1";
        String webhook = "https://example.com/webhook";
        aggregator.initBatch(batchId, 2, webhook);

        var response1 = buildResponse("v1");
        var response2 = buildResponse("v2");

        try (MockedStatic<ThetaRestClient> theta = Mockito.mockStatic(ThetaRestClient.class)) {
            aggregator.recordResult(batchId, response1, webhook);
            aggregator.recordResult(batchId, response2, webhook);

            theta.verify(() -> ThetaRestClient.sendPostRequest(eq(webhook), any(), any(), eq(Void.class)));
        }
    }

    private CheckVideoUploadResponse buildResponse(String id) {
        var video = new Video();
        video.setId(id);
        var body = new CheckVideoUploadResponse.Body();
        body.setVideos(List.of(video));
        var resp = new CheckVideoUploadResponse();
        resp.setBody(body);
        return resp;
    }
}


