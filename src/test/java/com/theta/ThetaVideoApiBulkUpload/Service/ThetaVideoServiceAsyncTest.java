package com.theta.ThetaVideoApiBulkUpload.Service;

import com.theta.ThetaVideoApiBulkUpload.apiModels.UploadJobMessage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ThetaVideoServiceAsyncTest {

    @Test
    void enqueuesAllFilesAsJobs() {
        BulkUploadProducer producer = mock(BulkUploadProducer.class);
        UploadResultAggregator aggregator = mock(UploadResultAggregator.class);
        ThetaVideoService service = new ThetaVideoService(producer, aggregator);

        List<byte[]> files = List.of(new byte[]{1}, new byte[]{2}, new byte[]{3});
        service.processBulkUploadAsync(files, "key", "secret", "webhook");

        verify(aggregator).initBatch(any(), eq(3), eq("webhook"));
        verify(producer, times(3)).sendUploadJob(any(UploadJobMessage.class));
    }
}


