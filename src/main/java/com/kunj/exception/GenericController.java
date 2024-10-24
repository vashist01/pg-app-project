package com.kunj.exception;

import com.kunj.dto.response.ErrorBo;
import com.kunj.dto.response.ResponseBO;
import com.kunj.dto.response.ResponseErrorBO;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GenericController {

  protected <T> ResponseEntity<ResponseBO<T>> sendResponse(T data, String message,
      HttpServletRequest request) {

    return new ResponseEntity<>(ResponseBO.<T>builder().data(data).message(message)
        .requestTime(LocalDateTime.now().toString()).success(true).build(), HttpStatus.OK);
  }

  public <T> ResponseEntity<ResponseErrorBO<T>> sendFailure(T data, ApiErrorMessageResponse errors, int httpStatus,
      HttpServletRequest request) {

    return new ResponseEntity<>(ResponseErrorBO.<T>builder().data(data).errorBo(errors)
        .requestTime(LocalDateTime.now().toString()).success(false).build(),
        HttpStatus.OK);
  }
}