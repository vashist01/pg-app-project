package com.kunj.exception.custome;

public class FileUploadException extends BaseException{

  /**
   * Instantiates a new Base exception.
   *
   * @param errorMessage the error message
   * @param errorCode    the error code
   */
  public FileUploadException(String errorMessage, String errorCode) {
    super(errorMessage, errorCode);
  }
}
