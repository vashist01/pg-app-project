package com.kunj.exception;

import com.kunj.exception.custome.BaseException;


/**
 * The type No data found exception.
 */
public class NoDataFoundException extends BaseException {

  /**
   * Instantiates a new No data found exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public NoDataFoundException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
