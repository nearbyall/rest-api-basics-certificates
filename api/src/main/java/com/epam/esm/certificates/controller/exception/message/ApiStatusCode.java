package com.epam.esm.certificates.controller.exception.message;

public enum ApiStatusCode {

    CERTIFICATE_NOT_FOUND(40401),
    CERTIFICATE_BAD_REQUEST(40001),
    TAG_NOT_FOUND(40402),
    TAG_BAD_REQUEST(40002);

    private final int value;

    ApiStatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
