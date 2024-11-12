package com.kunj;

public class ResponseMessageConstant {

  public static final String SUCCESS = "successfully";
  public  static  final String DELIMITER = "/" ;
  public static final String BAD_CREDENTIALS = "Bad Credentials.";
  public static final String ERROR_CODE = "8001003";
  public static final String FILE_UPLOAD_ERROR_MESSAGE = "Failed to upload file in s3";
  public static final String FILE_TO_READ_FILE_FROM_S3_ERROR_MESSAGE = "Failed to read file in s3";
  public static final String IMAGE_PATH_PREFIX = "property-images"+ ResponseMessageConstant.DELIMITER;
  public static final String INVALID_OTP = "Otp is not matched with mobile number";
  public static final String INVALID_MOBILE_NUMBER = "The mobile number you entered does not exist in our records. Please check the number or register for a new account.don't have an account.";
  public static final String FILE_UPLOAD_LIMIT_EXCEED = "upload Property image limit exceeds consider upgrading your package to allow uploads property image";
  public static final String USER_ALREADY_EXIST = "User already register with this mobile Number : ";
  public static final String INVALID_USER = "The username or password is incorrect";
}
