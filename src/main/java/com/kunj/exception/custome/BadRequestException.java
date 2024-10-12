package com.kunj.exception.custome;

public class BadRequestException extends BaseException{

  /**
   * Instantiates a new Base exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public BadRequestException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
