package com.kunj.service;

import com.kunj.dto.request.UserDto;
import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.request.VerifyOtpDto;
import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.dto.response.UserResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface User service.
 */
public interface UserService {


  /**
   * Verify otp user response.
   *
   * @param verifyOtpDto the verify otp dto
   * @return the user response
   */
  UserResponse verifyOtp(VerifyOtpDto verifyOtpDto);

  /**
   * Login user response.
   *
   * @param userDto the user dto
   * @return the user response
   */
  UserResponse login(UserDto userDto);
}
