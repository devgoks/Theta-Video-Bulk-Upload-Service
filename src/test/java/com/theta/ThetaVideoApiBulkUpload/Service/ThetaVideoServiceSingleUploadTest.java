package com.theta.ThetaVideoApiBulkUpload.Service;

import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.ProcessedVideoResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.Video;
import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse.Body;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ThetaVideoServiceSingleUploadTest {

    @InjectMocks
    private ThetaVideoService thetaVideoService;

    @BeforeEach
    void setUp() {
        // Setup for tests
    }

    @Test
    void testCompileSingleProcessedVideoResponse_Success() {
        // Arrange
        CheckVideoUploadResponse checkVideoUploadResponse = createMockCheckVideoUploadResponse();

        // Act
        ProcessedVideoResponse result = thetaVideoService.compileSingleProcessedVideoResponse(checkVideoUploadResponse);

        // Assert
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Operation Successful", result.getMessage());
        assertNotNull(result.getVideos());
        assertEquals(1, result.getVideos().size());
        assertEquals("test-video-id", result.getVideos().get(0).getId());
    }

    @Test
    void testCompileSingleProcessedVideoResponse_WithMultipleVideos() {
        // Arrange
        CheckVideoUploadResponse checkVideoUploadResponse = createMockCheckVideoUploadResponseWithMultipleVideos();

        // Act
        ProcessedVideoResponse result = thetaVideoService.compileSingleProcessedVideoResponse(checkVideoUploadResponse);

        // Assert
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Operation Successful", result.getMessage());
        assertNotNull(result.getVideos());
        assertEquals(1, result.getVideos().size()); // Should only take the first video
        assertEquals("test-video-id-1", result.getVideos().get(0).getId());
    }

    @Test
    void testCompileSingleProcessedVideoResponse_WithNullVideos() {
        // Arrange
        CheckVideoUploadResponse checkVideoUploadResponse = new CheckVideoUploadResponse();
        Body body = new Body();
        body.setVideos(null);
        checkVideoUploadResponse.setBody(body);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            thetaVideoService.compileSingleProcessedVideoResponse(checkVideoUploadResponse);
        });
    }

    @Test
    void testCompileSingleProcessedVideoResponse_WithEmptyVideos() {
        // Arrange
        CheckVideoUploadResponse checkVideoUploadResponse = new CheckVideoUploadResponse();
        Body body = new Body();
        body.setVideos(List.of());
        checkVideoUploadResponse.setBody(body);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> {
            thetaVideoService.compileSingleProcessedVideoResponse(checkVideoUploadResponse);
        });
    }

    private CheckVideoUploadResponse createMockCheckVideoUploadResponse() {
        CheckVideoUploadResponse response = new CheckVideoUploadResponse();
        Body body = new Body();
        Video video = new Video();
        video.setId("test-video-id");
        video.setState("success");
        video.setProgress(100);
        video.setDuration("30526");
        video.setPlaybackUri("https://media.thetavideoapi.com/test-video-id/master.m3u8");
        video.setPlayerUri("https://player.thetavideoapi.com/video/test-video-id");
        body.setVideos(List.of(video));
        response.setBody(body);
        return response;
    }

    private CheckVideoUploadResponse createMockCheckVideoUploadResponseWithMultipleVideos() {
        CheckVideoUploadResponse response = new CheckVideoUploadResponse();
        Body body = new Body();
        Video video1 = new Video();
        video1.setId("test-video-id-1");
        video1.setState("success");
        video1.setProgress(100);
        
        Video video2 = new Video();
        video2.setId("test-video-id-2");
        video2.setState("success");
        video2.setProgress(100);
        
        body.setVideos(List.of(video1, video2));
        response.setBody(body);
        return response;
    }
}
