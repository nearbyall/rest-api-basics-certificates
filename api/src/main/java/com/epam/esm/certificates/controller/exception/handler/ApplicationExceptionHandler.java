package com.epam.esm.certificates.controller.exception.handler;

import com.epam.esm.certificates.controller.exception.ApiErrorResponse;
import com.epam.esm.certificates.controller.exception.ApiException;
import com.epam.esm.certificates.controller.exception.BadRequestException;
import com.epam.esm.certificates.controller.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class, BadRequestException.class})
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException e) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                e.getMessage(),
                e.getApiStatusCode().getValue()
        );

        return new ResponseEntity<>(apiErrorResponse, e.getHttpStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerException(NoHandlerFoundException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                messageSource.getMessage("controllerMessage.notFound", null, LocaleContextHolder.getLocale()),
                404
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                messageSource.getMessage("controllerMessage.serverError", null, LocaleContextHolder.getLocale()),
                500
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
