package com.theta.ThetaVideoApiBulkUpload.thetaRestClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ThetaRestClient {

    private static final RestTemplate restTemplate = new RestTemplate();

    public static <T> T sendPostRequest(String url, HttpHeaders headers, Object requestObject, Class<T> responseType) {
        var entity = new HttpEntity<>(requestObject, headers);
        var responseEntity = restTemplate.postForEntity(url, entity, responseType);

        log.info("Response HTTP Status Code: " + responseEntity.getStatusCodeValue());
        log.info("Response Headers: " + responseEntity.getHeaders());
        if(responseEntity.getBody() != null)
            log.info("Response Body: " + responseEntity.getBody().toString());
        return responseEntity.getBody();
    }

    public static <T> T sendGetRequest(String url, HttpHeaders headers,Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        var responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
        log.info("Response HTTP Status Code: " + responseEntity.getStatusCodeValue());
        log.info("Response Headers: " + responseEntity.getHeaders());
        if(responseEntity.getBody() != null)
            log.info("Response Body: " + responseEntity.getBody().toString());
        return responseEntity.getBody();
    }

    private static HttpHeaders getHeaders(){
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
