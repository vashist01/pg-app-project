package com.kunj.exception.custome;

public class IllegalArgumentException extends BaseException{

  /**
   * Instantiates a new Base exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public IllegalArgumentException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
