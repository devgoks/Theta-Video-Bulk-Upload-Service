package com.theta.ThetaVideoApiBulkUpload.soap;

import com.theta.ThetaVideoApiBulkUpload.Service.ThetaVideoService;
import com.theta.ThetaVideoApiBulkUpload.apiModels.ProcessedVideoResponse;
import com.theta.ThetaVideoApiBulkUpload.soap.gen.BulkUploadRequest;
import com.theta.ThetaVideoApiBulkUpload.soap.gen.BulkUploadResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Endpoint
public class BulkUploadSoapEndpoint {

    private static final String NAMESPACE_URI = "http://theta.com/bulkupload";

    private final ThetaVideoService thetaVideoService;

    public BulkUploadSoapEndpoint(ThetaVideoService thetaVideoService) {
        this.thetaVideoService = thetaVideoService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BulkUploadRequest")
    @ResponsePayload
    public BulkUploadResponse handleBulkUpload(@RequestPayload BulkUploadRequest request)
            throws IOException, InterruptedException, ExecutionException {

        // Convert uploaded files (already base64-encoded byte[])
        List<byte[]> listOfFilesBytes = request.getFiles()
                .stream()
                .map(f -> f)
                .collect(Collectors.toList());

        ProcessedVideoResponse processedVideoResponse;

        if (request.isAsyncFlow()) {
            thetaVideoService.processBulkUploadAsync(
                    listOfFilesBytes,
                    request.getThetaApiKey(),
                    request.getThetaApiSecret(),
                    request.getWebhookUrl()
            );
            processedVideoResponse = new ProcessedVideoResponse();
            processedVideoResponse.setStatus("success");
            processedVideoResponse.setMessage("Operation In Progress, You will get the final response on your webhook");
        } else {
            var checkVideoUploadResponses = thetaVideoService.processBulkUpload(
                    listOfFilesBytes,
                    request.getThetaApiKey(),
                    request.getThetaApiSecret()
            );
            processedVideoResponse = thetaVideoService.compileProcessedVideoResponse(checkVideoUploadResponses);
        }

        // Map to SOAP response
        BulkUploadResponse response = new BulkUploadResponse();
        response.setStatus(processedVideoResponse.getStatus());
        response.setMessage(processedVideoResponse.getMessage());

        return response;
    }
}
