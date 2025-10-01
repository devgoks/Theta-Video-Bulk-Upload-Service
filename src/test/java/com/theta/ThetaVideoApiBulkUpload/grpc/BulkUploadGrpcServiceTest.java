package com.theta.ThetaVideoApiBulkUpload.grpc;

import com.theta.ThetaVideoApiBulkUpload.Service.ThetaVideoService;
import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.ProcessedVideoResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.Video;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BulkUploadGrpcServiceTest {

    private Server server;
    private ManagedChannel channel;

    @BeforeEach
    public void setup() throws IOException {
        // no-op, created per test
    }

    @AfterEach
    public void teardown() {
        if (channel != null) {
            channel.shutdownNow();
        }
        if (server != null) {
            server.shutdownNow();
        }
    }

    @Test
    public void testProcessBulkUpload_sync() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        ThetaVideoService mockService = Mockito.mock(ThetaVideoService.class);

        Video v = new Video();
        v.setId("vid-1");
        List<Video> videos = new ArrayList<>();
        videos.add(v);
        ProcessedVideoResponse processed = new ProcessedVideoResponse();
        processed.setStatus("success");
        processed.setMessage("Operation Successful");
        processed.setVideos(videos);

        when(mockService.processBulkUpload(anyList(), anyString(), anyString()))
                .thenReturn(new ArrayList<CheckVideoUploadResponse>());
        when(mockService.compileProcessedVideoResponse(anyList())).thenReturn(processed);

        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new BulkUploadGrpcService(mockService))
                .build()
                .start();

        channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();

        BulkUploadServiceGrpc.BulkUploadServiceBlockingStub stub = BulkUploadServiceGrpc.newBlockingStub(channel);

        BulkUploadRequest req = BulkUploadRequest.newBuilder()
                .addFiles(com.google.protobuf.ByteString.copyFrom(new byte[]{1,2,3}))
                .setThetaApiKey("k")
                .setThetaApiSecret("s")
                .build();

        com.theta.ThetaVideoApiBulkUpload.grpc.ProcessedVideoResponse res = stub.processBulkUpload(req);
        Assertions.assertEquals("success", res.getStatus());
        Assertions.assertEquals(1, res.getDataCount());
        Assertions.assertEquals("vid-1", res.getData(0).getId());
    }

    @Test
    public void testProcessBulkUpload_async() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        ThetaVideoService mockService = Mockito.mock(ThetaVideoService.class);

        doNothing().when(mockService).processBulkUploadAsync(anyList(), anyString(), anyString(), anyString());

        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new BulkUploadGrpcService(mockService))
                .build()
                .start();

        channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();

        BulkUploadServiceGrpc.BulkUploadServiceBlockingStub stub = BulkUploadServiceGrpc.newBlockingStub(channel);

        BulkUploadRequest req = BulkUploadRequest.newBuilder()
                .addFiles(com.google.protobuf.ByteString.copyFrom(new byte[]{1,2,3}))
                .setThetaApiKey("k")
                .setThetaApiSecret("s")
                .setWebhookUrl("http://example.com/webhook")
                .build();

        OperationStarted started = stub.processBulkUploadAsync(req);
        Assertions.assertEquals("success", started.getStatus());
        Assertions.assertTrue(started.getMessage().toLowerCase().contains("in progress"));
    }
}


