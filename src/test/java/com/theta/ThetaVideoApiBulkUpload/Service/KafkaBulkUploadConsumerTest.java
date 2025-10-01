package com.theta.ThetaVideoApiBulkUpload.Service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.UploadJobMessage;
import com.theta.ThetaVideoApiBulkUpload.apiModels.Video;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class KafkaBulkUploadConsumerTest {

    @Test
    void processesMessageAndSendsToAggregator() throws UnirestException, InterruptedException {
        ThetaVideoService service = mock(ThetaVideoService.class);
        UploadResultAggregator aggregator = mock(UploadResultAggregator.class);

        KafkaBulkUploadConsumer consumer = new KafkaBulkUploadConsumer(service, aggregator);

        UploadJobMessage msg = UploadJobMessage.builder()
                .batchId("b1").fileBytes(new byte[]{1}).thetaApiKey("k").thetaApiSecret("s").webhookUrl("w")
                .build();

        CheckVideoUploadResponse resp = new CheckVideoUploadResponse();
        CheckVideoUploadResponse.Body body = new CheckVideoUploadResponse.Body();
        Video v = new Video(); v.setId("v1");
        body.setVideos(List.of(v));
        resp.setBody(body);

        when(service.combinedVideoUploadProcess(any(), any(), any())).thenReturn(resp);

        consumer.handleUploadJob(msg);

        verify(aggregator).recordResult(eq("b1"), eq(resp), eq("w"));
    }
}


