package com.kunj.exception.custome;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Base exception.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {

  private final String errorMessage;
  private final String errorCode;

  /**
   * Instantiates a new Base exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public BaseException(String errorMessage, String errorCode) {
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
  }
}
