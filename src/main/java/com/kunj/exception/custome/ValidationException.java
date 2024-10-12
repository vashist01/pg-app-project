package com.kunj.exception.custome;

/**
 * The type Validation exception.
 */
public class ValidationException extends BaseException {

  /**
   * Instantiates a new Validation exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public ValidationException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
