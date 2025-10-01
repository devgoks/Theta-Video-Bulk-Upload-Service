package com.theta.ThetaVideoApiBulkUpload.apiModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadJobMessage {
    private String batchId;
    private byte[] fileBytes;
    private String thetaApiKey;
    private String thetaApiSecret;
    private String webhookUrl;
}


