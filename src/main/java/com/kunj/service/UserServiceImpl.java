package com.kunj.service;

import com.kunj.ResponseMessageConstant;
import com.kunj.config.PropertyConfig;
import com.kunj.dto.request.UserDto;
import com.kunj.dto.request.VerifyOtpDto;
import com.kunj.dto.response.UserResponse;
import com.kunj.entity.Otp;
import com.kunj.entity.User;
import com.kunj.enums.ValidationEnum;
import com.kunj.exception.NoDataFoundException;
import com.kunj.exception.custome.InValidMobileNumberException;
import com.kunj.repository.OtpRepository;
import com.kunj.repository.UserRepository;
import com.kunj.util.ConvertorUtil;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type User service.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ConvertorUtil convertorUtil;
  private final PropertyConfig propertyConfig;
  private final OtpService otpService;
  private final OtpRepository otpRepository;

  /**
   * Instantiates a new User service.
   *
   * @param userRepository the user repository
   * @param convertorUtil  the convertor util
   * @param propertyConfig the property config
   * @param otpService     the otp service
   * @param otpRepository1 the otp repository 1
   */
  public UserServiceImpl(UserRepository userRepository,
      ConvertorUtil convertorUtil, final PropertyConfig propertyConfig,
      OtpService otpService,
      OtpRepository otpRepository1) {
    this.userRepository = userRepository;
    this.convertorUtil = convertorUtil;
    this.propertyConfig = propertyConfig;
    this.otpService = otpService;
    this.otpRepository = otpRepository1;

  }


  @Override
  public UserResponse login(UserDto userDto) {
    log.info("User login method is being executed for mobile number: {}", userDto);

    Optional<User> userOptional = userRepository.findByMobileNumber(userDto.getMobileNumber());
    if (userOptional.isEmpty()) {
      throw new InValidMobileNumberException(ResponseMessageConstant.INVALID_MOBILE_NUMBER,
          ResponseMessageConstant.ERROR_CODE);
    }
    User user = userOptional.get();
    return otpService.genrateOtp(user);
  }

  @Override
  public UserResponse verifyOtp(VerifyOtpDto verifyOtpDto) {
    String verifyOtp = verifyOtpDto.getOtp();
    String mobileNumber = convertorUtil.convertCUIDNumberToMobile(verifyOtpDto.getMobileNumber());

    Optional<Otp> optionalOtp = otpRepository.findByOtpAndMobileNumber(verifyOtp, mobileNumber);
    if (optionalOtp.isEmpty()) {
      throw new NoDataFoundException(ResponseMessageConstant.INVALID_OTP,
          ResponseMessageConstant.ERROR_CODE);
    }
    LocalDateTime otpCreationTime = optionalOtp.get().getCreatedAt();

    if (verifyOtp.equalsIgnoreCase(optionalOtp.get().getOtp())) {
      LocalDateTime now = LocalDateTime.now();
      long minutesElapsed = ChronoUnit.MINUTES.between(otpCreationTime, now);
      if (minutesElapsed <= Integer.parseInt(propertyConfig.getVerifyOtpTiemDuration())) {
        log.info(ValidationEnum.OTP_VERIFIED_SUCCESSFULLY.getMessage());
        return UserResponse.builder().message(ValidationEnum.OTP_VERIFIED_SUCCESSFULLY.getMessage())
            .build();
      }
      return UserResponse.builder().message(ValidationEnum.OTP_EXPIRED.getMessage()).build();
    }
    throw new NoDataFoundException(ResponseMessageConstant.INVALID_OTP,
        ResponseMessageConstant.ERROR_CODE);
  }
}
