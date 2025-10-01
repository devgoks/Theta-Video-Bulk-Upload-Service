package com.theta.ThetaVideoApiBulkUpload.Service;

import com.theta.ThetaVideoApiBulkUpload.apiModels.UploadJobMessage;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

public class BulkUploadProducerTest {

    @Test
    void sendsMessageToConfiguredTopic() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, Object> template = mock(KafkaTemplate.class);

        BulkUploadProducer producer = new BulkUploadProducer(template);
        // inject topic via reflection (since @Value won't run in unit test)
        setField(producer, "uploadJobsTopic", "theta.upload.jobs");

        UploadJobMessage message = UploadJobMessage.builder()
                .batchId("b1").fileBytes(new byte[]{1}).thetaApiKey("k").thetaApiSecret("s").webhookUrl("w")
                .build();

        producer.sendUploadJob(message);

        verify(template).send(eq("theta.upload.jobs"), eq("b1"), eq(message));
    }

    private static void setField(Object target, String field, Object value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


