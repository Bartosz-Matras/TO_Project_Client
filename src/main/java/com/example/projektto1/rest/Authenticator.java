package com.example.projektto1.rest;

import com.example.projektto1.dto.OperatorCredentialsDto;
import com.example.projektto1.handler.AuthenticationResultHandler;

public interface Authenticator {

    void authenticate(OperatorCredentialsDto operatorCredentialsDto, AuthenticationResultHandler authenticationResultHandler);
}
