package com.kunj.exception;

import com.kunj.exception.custome.BadCredentialsException;
import com.kunj.exception.custome.BadRequestException;
import com.kunj.exception.custome.InValidMobileNumberException;
import com.kunj.exception.custome.InvalidException;
import com.kunj.exception.custome.InvalidOtpException;
import java.util.Date;
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
                badCredentialsException.getErrorCode()).errorLogDateTime(new Date()).build(),
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
                invalidOtpException.getErrorCode()).errorLogDateTime(new Date()).build(),
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
  public ResponseEntity<ApiErrorMessageResponse> badRequestException(
      InvalidOtpException invalidOtpException, WebRequest webRequest) {
    return new ResponseEntity<>(
        ApiErrorMessageResponse.builder().errorMessage(invalidOtpException.getErrorMessage())
            .errorCode(
                invalidOtpException.getErrorCode()).errorLogDateTime(new Date()).build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorMessageResponse> illegalArgumentException(
      InvalidOtpException invalidOtpException, WebRequest webRequest) {
    return new ResponseEntity<>(
        ApiErrorMessageResponse.builder().errorMessage(invalidOtpException.getErrorMessage())
            .errorCode(
                invalidOtpException.getErrorCode()).errorLogDateTime(new Date()).build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidException.class)
  public ResponseEntity<ApiErrorMessageResponse> invalidException(
      InvalidException invalidException, WebRequest webRequest) {
    return new ResponseEntity<>(
        ApiErrorMessageResponse.builder().errorMessage(invalidException.getErrorMessage())
            .errorCode(
                invalidException.getErrorCode()).errorLogDateTime(new Date()).build(),
        HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(InValidMobileNumberException.class)
  public ResponseEntity<ApiErrorMessageResponse> inValidMobileNumberException(
      InValidMobileNumberException inValidMobileNumberException, WebRequest webRequest) {
    return new ResponseEntity<>(
        ApiErrorMessageResponse.builder().errorMessage(inValidMobileNumberException.getErrorMessage())
            .errorCode(
                inValidMobileNumberException.getErrorCode()).errorLogDateTime(new Date()).build(),
        HttpStatus.BAD_REQUEST);
  }
}
