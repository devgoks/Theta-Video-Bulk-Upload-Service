package com.theta.ThetaVideoApiBulkUpload.Service;

import com.theta.ThetaVideoApiBulkUpload.thetaRestClient.ThetaRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThetaVideoServiceMockSingleUploadTest {

    @InjectMocks
    private ThetaVideoServiceMock thetaVideoServiceMock;

    private String testWebhookUrl;

    @BeforeEach
    void setUp() {
        testWebhookUrl = "https://example.com/webhook";
    }

    @Test
    void testMockedSingleResponse_Success() {
        // Act
        String result = thetaVideoServiceMock.mockedSingleResponse();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("status"));
        assertTrue(result.contains("success"));
        assertTrue(result.contains("message"));
        assertTrue(result.contains("Operation Successful"));
        assertTrue(result.contains("data"));
        assertTrue(result.contains("video_gy7e01nv71empt4ayi7chvn9v9"));
        assertTrue(result.contains("playback_uri"));
        assertTrue(result.contains("player_uri"));
    }

    @Test
    void testMockedSingleResponse_ContainsExpectedFields() {
        // Act
        String result = thetaVideoServiceMock.mockedSingleResponse();

        // Assert
        assertTrue(result.contains("\"id\":\"video_gy7e01nv71empt4ayi7chvn9v9\""));
        assertTrue(result.contains("\"duration\":\"30526\""));
        assertTrue(result.contains("\"state\":\"success\""));
        assertTrue(result.contains("\"progress\":100"));
        assertTrue(result.contains("\"playback_uri\":\"https://media.thetavideoapi.com/video_gy7e01nv71empt4ayi7chvn9v9/master.m3u8\""));
        assertTrue(result.contains("\"player_uri\":\"https://player.thetavideoapi.com/video/video_gy7e01nv71empt4ayi7chvn9v9\""));
    }

    @Test
    void testMockedSingleAsyncFlow_Success() throws InterruptedException {
        // Arrange
        try (MockedStatic<ThetaRestClient> mockedStatic = mockStatic(ThetaRestClient.class)) {
            mockedStatic.when(() -> ThetaRestClient.sendPostRequest(anyString(), any(HttpHeaders.class), anyString(), eq(Void.class)))
                    .thenReturn(null);

            // Act
            thetaVideoServiceMock.mockedSingleAsyncFlow(testWebhookUrl);

            // Assert
            mockedStatic.verify(() -> ThetaRestClient.sendPostRequest(
                    eq(testWebhookUrl), 
                    any(HttpHeaders.class), 
                    anyString(), 
                    eq(Void.class)
            ));
        }
    }

    @Test
    void testMockedSingleAsyncFlow_WithNullWebhook() throws InterruptedException {
        // Arrange
        try (MockedStatic<ThetaRestClient> mockedStatic = mockStatic(ThetaRestClient.class)) {
            mockedStatic.when(() -> ThetaRestClient.sendPostRequest(isNull(), any(HttpHeaders.class), anyString(), eq(Void.class)))
                    .thenReturn(null);

            // Act
            thetaVideoServiceMock.mockedSingleAsyncFlow(null);

            // Assert
            mockedStatic.verify(() -> ThetaRestClient.sendPostRequest(
                    isNull(), 
                    any(HttpHeaders.class), 
                    anyString(), 
                    eq(Void.class)
            ));
        }
    }

    @Test
    void testMockedSingleAsyncFlow_WithEmptyWebhook() throws InterruptedException {
        // Arrange
        String emptyWebhookUrl = "";
        try (MockedStatic<ThetaRestClient> mockedStatic = mockStatic(ThetaRestClient.class)) {
            mockedStatic.when(() -> ThetaRestClient.sendPostRequest(eq(emptyWebhookUrl), any(HttpHeaders.class), anyString(), eq(Void.class)))
                    .thenReturn(null);

            // Act
            thetaVideoServiceMock.mockedSingleAsyncFlow(emptyWebhookUrl);

            // Assert
            mockedStatic.verify(() -> ThetaRestClient.sendPostRequest(
                    eq(emptyWebhookUrl), 
                    any(HttpHeaders.class), 
                    anyString(), 
                    eq(Void.class)
            ));
        }
    }

    @Test
    void testMockedSingleResponse_IsValidJson() {
        // Act
        String result = thetaVideoServiceMock.mockedSingleResponse();

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
        assertTrue(result.contains("\"status\":\"success\""));
        assertTrue(result.contains("\"message\":\"Operation Successful\""));
    }

    @Test
    void testMockedSingleResponse_ContainsSingleVideo() {
        // Act
        String result = thetaVideoServiceMock.mockedSingleResponse();

        // Assert
        // Count occurrences of video ID to ensure only one video is returned
        long videoIdCount = result.chars()
                .mapToObj(c -> (char) c)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString()
                .split("video_gy7e01nv71empt4ayi7chvn9v9")
                .length - 1;
        
        assertEquals(1, videoIdCount, "Should contain exactly one video ID");
    }

    @Test
    void testMockedSingleAsyncFlow_ThreadSleep() throws InterruptedException {
        // Arrange
        long startTime = System.currentTimeMillis();
        
        try (MockedStatic<ThetaRestClient> mockedStatic = mockStatic(ThetaRestClient.class)) {
            mockedStatic.when(() -> ThetaRestClient.sendPostRequest(anyString(), any(HttpHeaders.class), anyString(), eq(Void.class)))
                    .thenReturn(null);

            // Act
            thetaVideoServiceMock.mockedSingleAsyncFlow(testWebhookUrl);
            
            // Assert
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // Should sleep for approximately 30 seconds (allowing some tolerance)
            assertTrue(duration >= 29000, "Should sleep for at least 29 seconds");
            assertTrue(duration <= 31000, "Should not sleep for more than 31 seconds");
        }
    }
}
