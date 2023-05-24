package com.theta.ThetaVideoApiBulkUpload.controller;

import com.theta.ThetaVideoApiBulkUpload.Service.ThetaVideoService;
import com.theta.ThetaVideoApiBulkUpload.Service.ThetaVideoServiceMock;
import com.theta.ThetaVideoApiBulkUpload.apiModels.ProcessedVideoResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class BulkUploadController {

    private final ThetaVideoService thetaVideoService;

    private final ThetaVideoServiceMock thetaVideoServiceMock;

    public BulkUploadController(ThetaVideoService thetaVideoService, ThetaVideoServiceMock thetaVideoServiceMock) {
        this.thetaVideoService = thetaVideoService;
        this.thetaVideoServiceMock = thetaVideoServiceMock;
    }

    @PostMapping(value={"/theta/bulkupload","/theta/bulkupload/main"})
    public Object thetaBulkUpload(@RequestPart("files") MultipartFile[] files,
                                             @RequestPart("theta-api-key") String thetaApiKey,
                                             @RequestPart("theta-api-secret") String thetaApiSecret,
                                             @RequestPart("async-flow") String isAsyncFlow,
                                             @RequestPart(value = "webhook-url", required = false) String webhookUrl
                                             ) throws IOException, InterruptedException, ExecutionException {
        //copied files bytes is needed to prevent file not found exception
        // when the request thread returns and the async thread flow is still processing the upload
        var processedVideoResponse = new ProcessedVideoResponse();
        List<byte[]> listOfFilesBytes = new ArrayList<>();
        for (MultipartFile file : files) {
            listOfFilesBytes.add(file.getBytes());
        }
        if(Boolean.parseBoolean(isAsyncFlow)){
            thetaVideoService.processBulkUploadAsync(listOfFilesBytes,thetaApiKey,thetaApiSecret,webhookUrl);
            processedVideoResponse.setStatus("success");
            processedVideoResponse.setMessage("Operation In Progress, You will get the final response on your webhook");
        }else{
            var checkVideoUploadResponses = thetaVideoService.processBulkUpload(listOfFilesBytes,thetaApiKey,thetaApiSecret);
            processedVideoResponse = thetaVideoService.compileProcessedVideoResponse(checkVideoUploadResponses);
        }
        return processedVideoResponse;
    }


    @PostMapping(path="/theta/bulkupload/mock", produces="application/json")
    public Object thetaBulkUploadMock(@RequestPart("files") MultipartFile[] files,
                                             @RequestPart("theta-api-key") String thetaApiKey,
                                             @RequestPart("theta-api-secret") String thetaApiSecret,
                                             @RequestPart("async-flow") String isAsyncFlow,
                                             @RequestPart(value = "webhook-url", required = false) String webhookUrl
    ) throws IOException, InterruptedException, ExecutionException {
        if(Boolean.parseBoolean(isAsyncFlow)){
            thetaVideoServiceMock.mockedAsyncFlow(webhookUrl);
            var processedVideoResponse = new ProcessedVideoResponse();
            processedVideoResponse.setStatus("success");
            processedVideoResponse.setMessage("Operation In Progress, You will get the final response on your webhook");
            return processedVideoResponse;
        }else{
            Thread.sleep(30000);
            return thetaVideoServiceMock.mockedResponse();
        }
    }
}
