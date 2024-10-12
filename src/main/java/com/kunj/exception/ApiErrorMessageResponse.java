package com.kunj.exception;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Api error message response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorMessageResponse {

  private String errorMessage;
  private String errorCode;
  private String status;
  private Date errorLogDateTime;
}
