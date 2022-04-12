package com.epam.esm.certificates.controller.exception;

import com.epam.esm.certificates.controller.exception.message.ApiStatusCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends  ApiException {

    public BadRequestException(String message, ApiStatusCode apiStatusCode, HttpStatus httpStatus) {
        super(message, apiStatusCode, httpStatus);
    }

}
