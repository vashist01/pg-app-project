package com.kunj.exception.custome;

/**
 * The type Invalid otp exception.
 */
public class InvalidOtpException extends BaseException {

  /**
   * Instantiates a new Invalid otp exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public InvalidOtpException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
