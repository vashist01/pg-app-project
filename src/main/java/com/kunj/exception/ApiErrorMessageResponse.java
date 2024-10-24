package com.kunj.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
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

  @JsonProperty("error_message")
  private String errorMessage;
  @JsonProperty("error_code")
  private String errorCode;
}
