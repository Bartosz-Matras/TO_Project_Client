package com.example.projektto1.rest;

import com.example.projektto1.dto.OperatorAuthenticationResultDto;
import com.example.projektto1.handler.AuthenticationResultHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.example.projektto1.dto.OperatorCredentialsDto;

public class AuthenticatorImpl implements Authenticator{

    private final RestTemplate restTemplate;
    private static final String AUTHENTICATION_URL = "http://localhost:8080/verify_operator_credentials";

    public AuthenticatorImpl(){
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void authenticate(OperatorCredentialsDto operatorCredentialsDto,
                             AuthenticationResultHandler authenticationResultHandler) {

        Runnable authenticationTask = () -> {
            processAuthentication(operatorCredentialsDto, authenticationResultHandler);
        };

        Thread authenticationThread = new Thread(authenticationTask);
        authenticationThread.setDaemon(true);
        authenticationThread.start();

    }

    private void processAuthentication(OperatorCredentialsDto operatorCredentialsDto,
                                              AuthenticationResultHandler authenticationResultHandler) {
        ResponseEntity<OperatorAuthenticationResultDto> responseEntity = restTemplate.postForEntity(AUTHENTICATION_URL, operatorCredentialsDto, OperatorAuthenticationResultDto.class);

//        OperatorAuthenticationResultDto dto = new OperatorAuthenticationResultDto();
//        dto.setAuthenticated(true);
//        dto.setFirstName("Bartek");
//        dto.setLastName("Matras");
//        dto.setIdOperator(1L);

        authenticationResultHandler.handle(responseEntity.getBody());
    }
}
