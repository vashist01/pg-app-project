package com.kunj.service;

import com.kunj.dto.request.VerifyOtpDto;
import com.kunj.dto.response.UserResponse;
import com.kunj.entity.User;

/**
 * The interface Otp service.
 */
public interface OtpService {

  /**
   * Verify otp user response.
   *
   * @param verifyOtpDto the verify otp dto
   * @return the user response
   */
  UserResponse verifyOtp(VerifyOtpDto verifyOtpDto);

  /**
   * Genrate otp user response.
   *
   * @param user the user
   * @return the user response
   */
  UserResponse genrateOtp(User user);
}
