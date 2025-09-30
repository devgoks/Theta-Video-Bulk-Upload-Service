package com.theta.ThetaVideoApiBulkUpload.Service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.theta.ThetaVideoApiBulkUpload.apiModels.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class ThetaVideoServiceTest {

    private ThetaVideoService service;

    @BeforeEach
    void setUp() {
        service = Mockito.spy(new ThetaVideoService());
    }

    @Test
    void compileProcessedVideoResponse_buildsExpectedResponse() {
        // Arrange
        var video = new Video();
        video.setId("vid_1");
        video.setProgress(100);
        video.setState("success");

        var body = new CheckVideoUploadResponse.Body();
        body.setVideos(List.of(video));
        var check = new CheckVideoUploadResponse();
        check.setStatus("success");
        check.setBody(body);

        // Act
        var result = service.compileProcessedVideoResponse(List.of(check));

        // Assert
        assertEquals("success", result.getStatus());
        assertEquals("Operation Successful", result.getMessage());
        assertNotNull(result.getVideos());
        assertEquals(1, result.getVideos().size());
        assertEquals("vid_1", result.getVideos().get(0).getId());
    }

    @Test
    void processBulkUpload_runsTasksAndAggregatesResults() throws ExecutionException, InterruptedException, UnirestException {
        // Arrange inputs
        List<byte[]> files = new ArrayList<>();
        files.add(new byte[]{1,2,3});
        files.add(new byte[]{4,5,6});

        // Stub combinedVideoUploadProcess to avoid network and control outputs
        var finalCheck = buildCheckVideoUploadResponse(100, "success", "transcoded_1");
        Mockito.doReturn(finalCheck)
                .when(service)
                .combinedVideoUploadProcess(Mockito.any(byte[].class), Mockito.anyString(), Mockito.anyString());

        // Act
        var results = service.processBulkUpload(files, "key", "secret");

        // Assert
        assertEquals(2, results.size());
        for (CheckVideoUploadResponse r : results) {
            assertEquals("success", r.getStatus());
            assertEquals(100, r.getBody().getVideos().get(0).getProgress());
        }
    }


    private static CreatePreSignedUrlResponse buildCreatePreSignedUrlResponse(String uploadId, String url) {
        var upload = new CreatePreSignedUrlResponse.Upload();
        upload.setId(uploadId);
        upload.setPresignedUrl(url);

        var body = new CreatePreSignedUrlResponse.Body();
        body.setUploads(List.of(upload));

        var resp = new CreatePreSignedUrlResponse();
        resp.setStatus("success");
        resp.setBody(body);
        return resp;
    }

    private static TranscodeVideoResponse buildTranscodeVideoResponse(String videoId) {
        var tv = new TranscodeVideoResponse.Body.Video();
        tv.setId(videoId);
        tv.setProgress(0);
        tv.setState("processing");

        var body = new TranscodeVideoResponse.Body();
        body.setVideos(List.of(tv));

        var resp = new TranscodeVideoResponse();
        resp.setStatus("success");
        resp.setBody(body);
        return resp;
    }

    private static CheckVideoUploadResponse buildCheckVideoUploadResponse(int progress, String state, String id) {
        var v = new Video();
        v.setId(id);
        v.setProgress(progress);
        v.setState(state);

        var body = new CheckVideoUploadResponse.Body();
        body.setVideos(List.of(v));

        var resp = new CheckVideoUploadResponse();
        resp.setStatus("success");
        resp.setBody(body);
        return resp;
    }
}


