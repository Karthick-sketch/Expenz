package com.karthick.Expenz.common;

public class ApiResponse {
    private int status;
    private Object data;
    private Object error;

    public ApiResponse() {
        this.status = 200;
        this.data = null;
        this.error = null;
    }

    public ApiResponse(Object data) {
        this.status = 200;
        this.data = data;
        this.error = null;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
