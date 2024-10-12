package com.kunj.exception.custome;

/**
 * The type Bad credentials exception.
 */
public class BadCredentialsException extends BaseException {

  /**
   * Instantiates a new Bad credentials exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public BadCredentialsException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
