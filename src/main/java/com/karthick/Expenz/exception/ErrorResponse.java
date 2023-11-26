package com.karthick.Expenz.exception;

import java.util.List;

public class ErrorResponse {
    private List<String> error;

    public ErrorResponse(List<String> errorMessage) {
        this.error = errorMessage;
    }

    public ErrorResponse(String errorMessage) {
        this.error = List.of(errorMessage);
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> errorMessage) {
        this.error = errorMessage;
    }
}
