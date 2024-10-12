package com.kunj.enums;

/**
 * The enum Constant enums.
 */
public enum ConstantEnums {
  /**
   * Mobile number constant enums.
   */
  MOBILE_NUMBER("mobile_number"),
  /**
   * Authorization constant enums.
   */
  AUTHORIZATION("Authorization"),
  /**
   * Bearer constant enums.
   */
  BEARER("Bearer "),
  /**
   * Device serial constant enums.
   */
  DEVICE_SERIAL(
      "device_serial_number"),
  /**
   * The Invalid auth header token.
   */
  INVALID_AUTH_HEADER_TOKEN("invalid token ."),
  /**
   * Exception error code constant enums.
   */
  EXCEPTION_ERROR_CODE("9001001"),
  /**
   * The String formate.
   */
  STRING_FORMATE(
      "Error GUID=%s; error message: %s"),
  /**
   * Dynamo db primary key value constant enums.
   */
  DYNAMO_DB_PRIMARY_KEY_VALUE(
      ":primaryKeyValue"),
  /**
   * With key condition expresssion constant enums.
   */
  WITH_KEY_CONDITION_EXPRESSSION("#mobile_number ="),
  /**
   * Token expire date constant enums.
   */
  TOKEN_EXPIRE_DATE(
      "token_expire_date"),
  /**
   * Has attribute names constant enums.
   */
  HAS_ATTRIBUTE_NAMES(
      "#"),
  /**
   * Properties prefix for secret manager constant enums.
   */
  PROPERTIES_PREFIX_FOR_SECRET_MANAGER("local-pg-app-key");
  private final String value;

  ConstantEnums(String value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }
}
