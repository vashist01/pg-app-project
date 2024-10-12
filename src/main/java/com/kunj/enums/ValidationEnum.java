package com.kunj.enums;

import lombok.Getter;

/**
 * The enum Validation enum.
 */
@Getter
public enum ValidationEnum {
  /**
   * Mobile number pattern validation enum.
   */
  MOBILE_NUMBER_PATTERN("\\d{10}"),
  /**
   * The Date time formate.
   */
  DATE_TIME_FORMATE("yyyy-MM-dd HH:mm:ss"),
  /**
   * The Otp verified successfully.
   */
  OTP_VERIFIED_SUCCESSFULLY(
      "Successfully verified Otp with mobile number."),
  /**
   * The Otp expired.
   */
  OTP_EXPIRED("OTP expired for mobile number."),
  /**
   * The Invalid aws secret manager key.
   */
  INVALID_AWS_SECRET_MANAGER_KEY(
      " You do not have permission to access or modify this secret. Please contact your administrator.");

  /**
   * The constant OTP_VALIDATION.
   */
  public static final String OTP_VALIDATION = "\\d{6}";

  /**
   * -- GETTER --
   *  Gets message.
   *
   */
  private final String message;

  ValidationEnum(String pattern) {
    this.message = pattern;
  }

  /**
   * Validate mobile number boolean.
   *
   * @param mobileNumber the mobile number
   * @return the boolean
   */
// Optionally, you can add a method to validate a mobile number using the pattern
  public boolean validateMobileNumber(String mobileNumber) {
    return mobileNumber.matches(message);
  }
}
