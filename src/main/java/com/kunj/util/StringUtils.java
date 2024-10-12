package com.kunj.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * The type String utils.
 */
@Component
@Slf4j
public class StringUtils {

  private StringUtils() {
    log.info("private constructor ...... ");
  }

  /**
   * Has length boolean.
   *
   * @param content the content
   * @return the boolean
   */
  public static boolean hasLength(String content) {
    return content != null && !content.isEmpty();
  }
}
