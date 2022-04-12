package com.epam.esm.certificates.controller.exception;

import com.epam.esm.certificates.controller.exception.message.ApiStatusCode;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message, ApiStatusCode apiStatusCode, HttpStatus httpStatus) {
        super(message, apiStatusCode, httpStatus);
    }

}
