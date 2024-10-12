package com.kunj.enums;

/**
 * The enum Validation message enum.
 */
public enum ValidationMessageEnum {
  /**
   * The Invalid mobile number.
   */
  INVALID_MOBILE_NUMBER("Invalid mobile number"),
  // Add more validation messages as needed
  ;

  private final String message;

  ValidationMessageEnum(String message) {
    this.message = message;
  }

  /**
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }
}
