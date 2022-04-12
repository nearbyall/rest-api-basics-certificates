package com.epam.esm.certificates.controller.exception;

public class ApiErrorResponse {

    private final String message;
    private final int status;

    public ApiErrorResponse(String message, int apiStatusCode) {
        this.message = message;
        this.status = apiStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiErrorResponse that = (ApiErrorResponse) o;

        if (status != that.status) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + status;
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }


}
