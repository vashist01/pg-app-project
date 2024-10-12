package com.kunj.enums;

/**
 * The enum Date time format enum.
 */
public enum DateTimeFormatEnum {

  /**
   * Date time without seconds date time format enum.
   */
  DATE_TIME_WITHOUT_SECONDS("yyyy-MM-dd'T'HH:mm"),
  /**
   * Date time format date time format enum.
   */
  DATE_TIME_FORMAT("yyyy-MM-dd'T'HH:mm:ss"),
  /**
   * The Date time with seconds format.
   */
  DATE_TIME_WITH_SECONDS_FORMAT("yyyy-MM-dd HH:mm:ss");

  private final String format;

  DateTimeFormatEnum(String format) {
    this.format = format;
  }

  /**
   * From value date time format enum.
   *
   * @param format the format
   * @return the date time format enum
   */
// Optionally, you can add a method to get an enum by format string
  public static DateTimeFormatEnum fromValue(String format) {
    for (DateTimeFormatEnum dtf : values()) {
      if (dtf.getValue().equals(format)) {
        return dtf;
      }
    }
    throw new IllegalArgumentException("Unknown format: " + format);
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public String getValue() {
    return format;
  }
}
