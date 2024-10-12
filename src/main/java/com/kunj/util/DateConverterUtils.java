package com.kunj.util;

import com.kunj.enums.DateTimeFormatEnum;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

/**
 * The type Date converter utils.
 */
@Component
public class DateConverterUtils {

  /**
   * The Formatter.
   */
  static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      DateTimeFormatEnum.DATE_TIME_WITH_SECONDS_FORMAT.getValue());

  /**
   * Convert local date time format local date time.
   *
   * @param createdAt the created at
   * @return the local date time
   */
  public static LocalDateTime convertLocalDateTimeFormat(LocalDateTime createdAt) {
    return convertStringToLocalDateTime(createdAt.format(formatter));
  }

  /**
   * Convert string to local date time local date time.
   *
   * @param dateTimeString the date time string
   * @return the local date time
   */
// Converts a formatted string back to LocalDateTime
  public static LocalDateTime convertStringToLocalDateTime(String dateTimeString) {
    return LocalDateTime.parse(dateTimeString, formatter);
  }

  /**
   * Gets current date time.
   *
   * @return the current date time
   */
  public static String getCurrentDateTime() {
    Timestamp currentTimestamp = Timestamp.from(Instant.now());
    LocalDateTime localDateTime = currentTimestamp.toLocalDateTime();

    return localDateTime.format(formatter);
  }

  /**
   * Gets expire token with 12 hrs.
   *
   * @return the expire token with 12 hrs
   */
  public static String getExpireTokenWith12Hrs() {
    Instant now = Instant.now();
    Instant next12Hours = now.plus(6000, ChronoUnit.HOURS);
    Timestamp next12HoursTimestamp = Timestamp.from(next12Hours);
    LocalDateTime next12HoursDateTime = next12HoursTimestamp.toLocalDateTime();
    return next12HoursDateTime.format(formatter);
  }

  /**
   * Is token valid boolean.
   *
   * @param tokenExpiredDate the token expired date
   * @return the boolean
   */
  public boolean isTokenValid(String tokenExpiredDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        DateTimeFormatEnum.DATE_TIME_WITH_SECONDS_FORMAT.getValue());
    LocalDate localDate = LocalDate.parse(tokenExpiredDate, formatter);
    return !LocalDate.now().isAfter(localDate);
  }
}
