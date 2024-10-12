package com.kunj.exception.custome;

/**
 * The type Invalid auth header exception.
 */
public class InvalidAuthHeaderException extends BaseException {


  /**
   * Instantiates a new Invalid auth header exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public InvalidAuthHeaderException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
