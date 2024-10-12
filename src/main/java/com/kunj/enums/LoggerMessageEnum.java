package com.kunj.enums;

import lombok.Getter;

/**
 * The enum Logger message enum.
 */
@Getter
public enum LoggerMessageEnum {
  /**
   * The Read data from dynamodb by mobile number logger.
   */
  READ_DATA_FROM_DYNAMODB_BY_MOBILE_NUMBER_LOGGER(
      "Starting readDataFromDynamoDbByMobileNumber method with mobileNumber: {} and tableName: {}"),
  /**
   * The Item list result logger.
   */
  ITEM_LIST_RESULT_LOGGER(
      "Starting getItemListResult method with queryRequest: {}  "),
  /**
   * The Invalid otp.
   */
  INVALID_OTP("Invalid OTP.");

  private final String message;

  LoggerMessageEnum(String message) {
    this.message = message;
  }

}