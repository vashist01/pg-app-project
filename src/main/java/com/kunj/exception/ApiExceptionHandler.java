package com.kunj.exception;

import com.kunj.dto.response.ResponseBO;
import com.kunj.dto.response.ResponseErrorBO;
import com.kunj.exception.custome.BadCredentialsException;
import com.kunj.exception.custome.BadRequestException;
import com.kunj.exception.custome.InValidMobileNumberException;
import com.kunj.exception.custome.InvalidException;
import com.kunj.exception.custome.InvalidOtpException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The type Api exception handler.
 */
@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  protected final GenericController genericController;

  public ApiExceptionHandler(GenericController genericController) {
    this.genericController = genericController;
  }

  /**
   * Bad credentials exception response entity.
   *
   * @param badCredentialsException the bad credentials exception
   * @param webRequest              the web request
   * @return the response entity
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiErrorMessageResponse> badCredentialsException(
      BadCredentialsException badCredentialsException, WebRequest webRequest) {
    return new ResponseEntity<>(
        ApiErrorMessageResponse.builder().errorMessage(badCredentialsException.getErrorMessage())
            .errorCode(
                badCredentialsException.getErrorCode()).build(),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Invalid otp exception response entity.
   *
   * @param invalidOtpException the invalid otp exception
   * @param webRequest          the web request
   * @return the response entity
   */
  @ExceptionHandler(InvalidOtpException.class)
  public ResponseEntity<ApiErrorMessageResponse> invalidOtpException(
      InvalidOtpException invalidOtpException, WebRequest webRequest) {
    return new ResponseEntity<>(
        ApiErrorMessageResponse.builder().errorMessage(invalidOtpException.getErrorMessage())
            .errorCode(
                invalidOtpException.getErrorCode()).build(),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Invalid otp exception response entity.
   *
   * @param invalidOtpException the invalid otp exception
   * @param webRequest          the web request
   * @return the response entity
   */
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ResponseErrorBO<Object>> badRequestException(
      InvalidOtpException invalidOtpException, HttpServletRequest webRequest) {
    return genericController.sendFailure(null,
        ApiErrorMessageResponse.builder().errorMessage(invalidOtpException.getErrorMessage())
            .errorCode(
                invalidOtpException.getErrorCode()).build(),
        HttpStatus.BAD_REQUEST.value(), webRequest);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResponseErrorBO<Object>> illegalArgumentException(
      InvalidOtpException invalidOtpException, HttpServletRequest webRequest) {
    return genericController.sendFailure(null,
        ApiErrorMessageResponse.builder().errorMessage(invalidOtpException.getErrorMessage())
            .errorCode(
                invalidOtpException.getErrorCode()).build(),
        HttpStatus.BAD_REQUEST.value(), webRequest);
  }

  @ExceptionHandler(InvalidException.class)
  public ResponseEntity<ResponseErrorBO<Object>> invalidException(
      InvalidException invalidException, HttpServletRequest webRequest) {
    return genericController.sendFailure(null,
        ApiErrorMessageResponse.builder().errorMessage(invalidException.getErrorMessage())
            .errorCode(
                invalidException.getErrorCode()).build(),
        HttpStatus.BAD_REQUEST.value(), webRequest);
  }

  @ExceptionHandler(InValidMobileNumberException.class)
  public ResponseEntity<ResponseErrorBO<Object>> handleMethodArgumentTypeMismatchException(
      InValidMobileNumberException inValidMobileNumberException,
      HttpServletRequest request) {

    return genericController.sendFailure(null,
        ApiErrorMessageResponse.builder().errorMessage(inValidMobileNumberException.getErrorMessage())
            .errorCode(
                inValidMobileNumberException.getErrorCode()).build(),
        HttpStatus.BAD_REQUEST.value(), request);
  }
}

