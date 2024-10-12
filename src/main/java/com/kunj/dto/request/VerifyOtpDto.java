package com.kunj.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kunj.enums.ValidationEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * The type Verify otp dto.
 */
@Data
public class VerifyOtpDto {

  @NotNull(message = "invalid otp. ")
  @Pattern(regexp = ValidationEnum.OTP_VALIDATION)
  private String otp;
  @JsonProperty("mobile_number")
  private String mobileNumber;
}