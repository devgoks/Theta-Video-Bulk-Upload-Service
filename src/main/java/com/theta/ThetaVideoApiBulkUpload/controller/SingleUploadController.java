package com.theta.ThetaVideoApiBulkUpload.controller;

import com.theta.ThetaVideoApiBulkUpload.Service.ThetaVideoService;
import com.theta.ThetaVideoApiBulkUpload.Service.ThetaVideoServiceMock;
import com.theta.ThetaVideoApiBulkUpload.apiModels.ProcessedVideoResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
public class SingleUploadController {

    private final ThetaVideoService thetaVideoService;
    private final ThetaVideoServiceMock thetaVideoServiceMock;

    public SingleUploadController(ThetaVideoService thetaVideoService, ThetaVideoServiceMock thetaVideoServiceMock) {
        this.thetaVideoService = thetaVideoService;
        this.thetaVideoServiceMock = thetaVideoServiceMock;
    }

    @PostMapping(value={"/theta/singleupload","/theta/singleupload/main"})
    public Object thetaSingleUpload(@RequestPart("file") MultipartFile file,
                                   @RequestPart("theta-api-key") String thetaApiKey,
                                   @RequestPart("theta-api-secret") String thetaApiSecret,
                                   @RequestPart("async-flow") String isAsyncFlow,
                                   @RequestPart(value = "webhook-url", required = false) String webhookUrl
    ) throws IOException, InterruptedException, ExecutionException, UnirestException {
        // Copy file bytes to prevent file not found exception
        // when the request thread returns and the async thread flow is still processing the upload
        var processedVideoResponse = new ProcessedVideoResponse();
        byte[] fileBytes = file.getBytes();
        
        if(Boolean.parseBoolean(isAsyncFlow)){
            thetaVideoService.processSingleUploadAsync(fileBytes, thetaApiKey, thetaApiSecret, webhookUrl);
            processedVideoResponse.setStatus("success");
            processedVideoResponse.setMessage("Operation In Progress, You will get the final response on your webhook");
        }else{
            var checkVideoUploadResponse = thetaVideoService.processSingleUpload(fileBytes, thetaApiKey, thetaApiSecret);
            processedVideoResponse = thetaVideoService.compileSingleProcessedVideoResponse(checkVideoUploadResponse);
        }
        return processedVideoResponse;
    }

    @PostMapping(path="/theta/singleupload/mock", produces="application/json")
    public Object thetaSingleUploadMock(@RequestPart("file") MultipartFile file,
                                       @RequestPart("theta-api-key") String thetaApiKey,
                                       @RequestPart("theta-api-secret") String thetaApiSecret,
                                       @RequestPart("async-flow") String isAsyncFlow,
                                       @RequestPart(value = "webhook-url", required = false) String webhookUrl
    ) throws IOException, InterruptedException, ExecutionException, UnirestException {
        if(Boolean.parseBoolean(isAsyncFlow)){
            thetaVideoServiceMock.mockedSingleAsyncFlow(webhookUrl);
            var processedVideoResponse = new ProcessedVideoResponse();
            processedVideoResponse.setStatus("success");
            processedVideoResponse.setMessage("Operation In Progress, You will get the final response on your webhook");
            return processedVideoResponse;
        }else{
            Thread.sleep(30000);
            return thetaVideoServiceMock.mockedSingleResponse();
        }
    }
}
