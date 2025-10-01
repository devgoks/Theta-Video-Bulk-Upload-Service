package com.theta.ThetaVideoApiBulkUpload.Service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.UploadJobMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaBulkUploadConsumer {

    private final ThetaVideoService thetaVideoService;

    private final UploadResultAggregator uploadResultAggregator;

    @Value("${app.kafka.topic.upload-results}")
    private String uploadResultsTopic;

    @KafkaListener(topics = "${app.kafka.topic.upload-jobs}", containerFactory = "kafkaListenerContainerFactory")
    public void handleUploadJob(UploadJobMessage message) throws UnirestException, InterruptedException {
        String batchId = message.getBatchId();
        byte[] fileBytes = message.getFileBytes();

        log.info("Consuming upload job for batch {}", batchId);
        CheckVideoUploadResponse result = thetaVideoService.combinedVideoUploadProcess(
                fileBytes,
                message.getThetaApiKey(),
                message.getThetaApiSecret()
        );

        uploadResultAggregator.recordResult(batchId, result, message.getWebhookUrl());
    }
}


