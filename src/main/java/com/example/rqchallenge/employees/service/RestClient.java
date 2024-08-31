package com.example.rqchallenge.employees.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RestClient {

    private final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

    private final RestTemplate restTemplate;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T execute(String uri, HttpMethod method, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        LOGGER.info("[RestService::execute] {} rest request received for URI {}", method.name(), uri);

        ResponseEntity<T> responseEntity = restTemplate.exchange(
                uri,
                method,
                entity,
                responseType,
                uriVariables
        );
        LOGGER.trace("[RestService::execute] {} request response for URI {} : {}", method.name(), uri, responseEntity.getBody());
        return responseEntity.getBody();
    }

    public <T> T execute(String uri, HttpMethod method, Class<T> responseType, Map<String, ?> uriVariables, Object requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
        LOGGER.info("[RestService::execute] {} rest request received for URI {}", method.name(), uri);

        ResponseEntity<T> responseEntity = restTemplate.exchange(
                uri,
                method,
                entity,
                responseType,
                uriVariables
        );
        LOGGER.trace("[RestService::execute] {} request response for URI {} : {}", method.name(), uri, responseEntity.getBody());
        return responseEntity.getBody();
    }

    public <T> T execute(String uri, HttpMethod method, Class<T> responseType) {
        return execute(uri, method, responseType, new HashMap<>());
    }
}

