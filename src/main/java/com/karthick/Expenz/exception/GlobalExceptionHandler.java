package com.karthick.Expenz.exception;

import com.karthick.Expenz.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleNoSuchElementException(NoSuchElementException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.NOT_FOUND);
        apiResponse.setError(e.getMessage());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.NOT_FOUND);
        apiResponse.setError(e.getMessage());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
