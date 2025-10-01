package com.theta.ThetaVideoApiBulkUpload.controller;

import com.theta.ThetaVideoApiBulkUpload.Service.ThetaVideoService;
import com.theta.ThetaVideoApiBulkUpload.Service.ThetaVideoServiceMock;
import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.ProcessedVideoResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.Video;
import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse.Body;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SingleUploadControllerTest {

    @Mock
    private ThetaVideoService thetaVideoService;

    @Mock
    private ThetaVideoServiceMock thetaVideoServiceMock;

    @InjectMocks
    private SingleUploadController singleUploadController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(singleUploadController).build();
    }

    @Test
    void testThetaSingleUpload_SyncFlow() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.mp4", "video/mp4", "test content".getBytes()
        );
        
        CheckVideoUploadResponse mockResponse = createMockCheckVideoUploadResponse();
        ProcessedVideoResponse mockProcessedResponse = createMockProcessedVideoResponse();
        
        when(thetaVideoService.processSingleUpload(any(byte[].class), anyString(), anyString()))
                .thenReturn(mockResponse);
        when(thetaVideoService.compileSingleProcessedVideoResponse(any(CheckVideoUploadResponse.class)))
                .thenReturn(mockProcessedResponse);

        // Act & Assert
        mockMvc.perform(multipart("/theta/singleupload")
                        .file(file)
                        .part(new org.springframework.mock.web.MockPart("theta-api-key", "test-key".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("theta-api-secret", "test-secret".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("async-flow", "false".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Operation Successful"));

        verify(thetaVideoService).processSingleUpload(any(byte[].class), eq("test-key"), eq("test-secret"));
        verify(thetaVideoService).compileSingleProcessedVideoResponse(any(CheckVideoUploadResponse.class));
    }

    @Test
    void testThetaSingleUpload_AsyncFlow() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.mp4", "video/mp4", "test content".getBytes()
        );

        doNothing().when(thetaVideoService).processSingleUploadAsync(any(byte[].class), anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(multipart("/theta/singleupload")
                        .file(file)
                        .part(new org.springframework.mock.web.MockPart("theta-api-key", "test-key".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("theta-api-secret", "test-secret".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("async-flow", "true".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("webhook-url", "https://example.com/webhook".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Operation In Progress, You will get the final response on your webhook"));

        verify(thetaVideoService).processSingleUploadAsync(any(byte[].class), eq("test-key"), eq("test-secret"), eq("https://example.com/webhook"));
    }

    @Test
    void testThetaSingleUploadMock_SyncFlow() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.mp4", "video/mp4", "test content".getBytes()
        );
        
        String mockResponse = "{\"status\":\"success\",\"message\":\"Operation Successful\"}";
        when(thetaVideoServiceMock.mockedSingleResponse()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(multipart("/theta/singleupload/mock")
                        .file(file)
                        .part(new org.springframework.mock.web.MockPart("theta-api-key", "test-key".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("theta-api-secret", "test-secret".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("async-flow", "false".getBytes())))
                .andExpect(status().isOk());

        verify(thetaVideoServiceMock).mockedSingleResponse();
    }

    @Test
    void testThetaSingleUploadMock_AsyncFlow() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.mp4", "video/mp4", "test content".getBytes()
        );

        doNothing().when(thetaVideoServiceMock).mockedSingleAsyncFlow(anyString());

        // Act & Assert
        mockMvc.perform(multipart("/theta/singleupload/mock")
                        .file(file)
                        .part(new org.springframework.mock.web.MockPart("theta-api-key", "test-key".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("theta-api-secret", "test-secret".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("async-flow", "true".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("webhook-url", "https://example.com/webhook".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Operation In Progress, You will get the final response on your webhook"));

        verify(thetaVideoServiceMock).mockedSingleAsyncFlow(eq("https://example.com/webhook"));
    }

    @Test
    void testThetaSingleUpload_WithoutWebhook() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.mp4", "video/mp4", "test content".getBytes()
        );

        doNothing().when(thetaVideoService).processSingleUploadAsync(any(byte[].class), anyString(), anyString(), isNull());

        // Act & Assert
        mockMvc.perform(multipart("/theta/singleupload")
                        .file(file)
                        .part(new org.springframework.mock.web.MockPart("theta-api-key", "test-key".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("theta-api-secret", "test-secret".getBytes()))
                        .part(new org.springframework.mock.web.MockPart("async-flow", "true".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Operation In Progress, You will get the final response on your webhook"));

        verify(thetaVideoService).processSingleUploadAsync(any(byte[].class), eq("test-key"), eq("test-secret"), isNull());
    }

    private CheckVideoUploadResponse createMockCheckVideoUploadResponse() {
        CheckVideoUploadResponse response = new CheckVideoUploadResponse();
        Body body = new Body();
        Video video = new Video();
        video.setId("test-video-id");
        video.setState("success");
        video.setProgress(100);
        body.setVideos(List.of(video));
        response.setBody(body);
        return response;
    }

    private ProcessedVideoResponse createMockProcessedVideoResponse() {
        ProcessedVideoResponse response = new ProcessedVideoResponse();
        response.setStatus("success");
        response.setMessage("Operation Successful");
        Video video = new Video();
        video.setId("test-video-id");
        video.setState("success");
        video.setProgress(100);
        response.setVideos(List.of(video));
        return response;
    }
}
