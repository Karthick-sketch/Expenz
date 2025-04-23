package com.karthick.Expenz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {
  private List<String> error;

  public ErrorResponse(String errorMessage) {
    this.error = List.of(errorMessage);
  }
}
