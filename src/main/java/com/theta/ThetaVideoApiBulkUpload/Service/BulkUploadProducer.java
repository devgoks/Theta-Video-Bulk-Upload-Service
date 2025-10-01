package com.theta.ThetaVideoApiBulkUpload.Service;

import com.theta.ThetaVideoApiBulkUpload.apiModels.UploadJobMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BulkUploadProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.upload-jobs}")
    private String uploadJobsTopic;

    public void sendUploadJob(UploadJobMessage message) {
        kafkaTemplate.send(uploadJobsTopic, message.getBatchId(), message);
        log.info("Published upload job for batch {}", message.getBatchId());
    }
}


