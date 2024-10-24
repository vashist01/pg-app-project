package com.kunj.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {

  @JsonProperty("mobile_number")
  private String mobileNumber;
  @JsonProperty("device_serial_number")
  private String deviceSerialNumber;
  @JsonProperty("device_token")
  private String deviceToken;
}