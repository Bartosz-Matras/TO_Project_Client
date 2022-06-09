package com.example.projektto1.rest;

import com.example.projektto1.dto.QuantityTypeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class QuantityTypeRestClient {

    private static final String QUANTITY_TYPES_URL = "http://localhost:8080/quantity_types";

    private final RestTemplate restTemplate;

    public QuantityTypeRestClient(){
        restTemplate = new RestTemplate();
    }

    public List<QuantityTypeDto> getQuantityTypes(){
        ResponseEntity<QuantityTypeDto[]> responseEntity
                = restTemplate.getForEntity(QUANTITY_TYPES_URL, QuantityTypeDto[].class);
        return Arrays.asList(responseEntity.getBody());
    }
}
