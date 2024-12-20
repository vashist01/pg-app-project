package com.kunj.controller;

import com.kunj.ResponseMessageConstant;
import com.kunj.dto.request.VerifyOtpDto;
import com.kunj.dto.response.ResponseBO;
import com.kunj.dto.response.UserResponse;
import com.kunj.enums.ApiEnum;
import com.kunj.exception.GenericController;
import com.kunj.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Otp controller.
 */
@RestController
@RequestMapping(ApiEnum.API_VERSION)
public class OtpController extends GenericController {

  private final OtpService otpService;

  /**
   * Instantiates a new Otp controller.
   *
   * @param otpService the otp service
   */
  public OtpController(OtpService otpService) {
    this.otpService = otpService;
  }

  /**
   * Verify otp response entity.
   *
   * @param verifyOtpDto the verify otp dto
   * @return the response entity
   */
  @Operation(summary = "verify User with Otp", description = "verify-otp")
  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(ApiEnum.VERIFY_OTP)
  public ResponseEntity<ResponseBO<UserResponse>> verifyOtp(@RequestBody @Valid VerifyOtpDto verifyOtpDto,
      HttpServletRequest httpServletRequest) {
    UserResponse userResponse = otpService.verifyOtp(verifyOtpDto);
    return sendResponse(userResponse, ResponseMessageConstant.SUCCESS,httpServletRequest);

  }
}
