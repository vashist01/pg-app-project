package com.kunj.exception.custome;

/**
 * The type In valid mobile number exception.
 */
public class InValidMobileNumberException extends BaseException {

  /**
   * Instantiates a new In valid mobile number exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public InValidMobileNumberException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
