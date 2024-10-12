package com.kunj.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Mapper.
 */
public class mapper {

  /**
   * The type User data.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserData {

    private Long id;
    private String mobileNumber;
    private String deviceSerialNumber;
    private String deviceToken;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private String createdBy;
    private String updateBy;
  }

  /**
   * The type User dto.
   */

}
