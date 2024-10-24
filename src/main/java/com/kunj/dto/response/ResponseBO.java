package com.kunj.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBO<T> {

  private T data;
  @JsonProperty("error")
  private ApiErrorMessageResponse errorBo;
  private String message;
  @JsonProperty("status_code")
  private int statusCode;
  @JsonProperty("request_time")
  private String requestTime;
  private boolean success;

}
