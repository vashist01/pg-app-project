package com.kunj.exception.custome;

public class InvalidException extends  BaseException{

  /**
   * Instantiates a new Base exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public InvalidException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
