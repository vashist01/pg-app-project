package com.kunj.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kunj.exception.ApiErrorMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseErrorBO<T> {
  private T data;
  @JsonProperty("error")
  private ApiErrorMessageResponse errorBo;
  @JsonProperty("request_time")
  private String requestTime;
  private boolean success;
  @JsonProperty("status_code")
  private int statusCode;
}
