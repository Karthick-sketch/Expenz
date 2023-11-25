package com.karthick.Expenz.common;

import org.springframework.http.HttpStatus;

public class ApiResponse {
    private HttpStatus status;
    private Object data;
    private Object error;

    public ApiResponse() {
        this.status = HttpStatus.OK;
        this.data = null;
        this.error = null;
    }

    public int getStatus() {
        return status.value();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
