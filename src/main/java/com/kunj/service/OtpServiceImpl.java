package com.kunj.service;

import com.kunj.dto.request.VerifyOtpDto;
import com.kunj.dto.response.UserResponse;
import com.kunj.entity.Otp;
import com.kunj.entity.User;
import com.kunj.entity.UserAuthToken;
import com.kunj.enums.LoggerMessageEnum;
import com.kunj.exception.custome.InValidMobileNumberException;
import com.kunj.exception.custome.InvalidOtpException;
import com.kunj.repository.OtpRepository;
import com.kunj.repository.UserAuthTokenRepository;
import com.kunj.util.CommonMethodUtil;
import com.kunj.util.DateConverterUtils;
import com.kunj.util.components.UserProfileRequestScop;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * The type Otp service.
 */
@Service
@Slf4j
public class OtpServiceImpl implements OtpService {

  private final UserProfileRequestScop userProfile;
  private final OtpRepository otpRepository;
  private final CommonMethodUtil commonMethodUtil;
  private final UserAuthTokenRepository userAuthTokenRepository;

  /**
   * Instantiates a new Otp service.
   *
   * @param userProfile             the user profile
   * @param otpRepository           the otp repository
   * @param commonMethodUtil        the common method util
   * @param userAuthTokenRepository the user auth token repository
   */
  public OtpServiceImpl(UserProfileRequestScop userProfile, OtpRepository otpRepository,
      CommonMethodUtil commonMethodUtil, UserAuthTokenRepository userAuthTokenRepository) {
    this.userProfile = userProfile;
    this.otpRepository = otpRepository;
    this.commonMethodUtil = commonMethodUtil;
    this.userAuthTokenRepository = userAuthTokenRepository;
  }

  /**
   * Generates a 6-digit OTP for the provided user and saves it in the repository.
   *
   * <p>
   * This method generates a 6-digit OTP using a utility method, associates it with the provided
   * user, and saves the OTP in the repository. It then constructs and returns a
   * {@code UserResponse} object containing the generated OTP.
   *
   * @param user the {@code User} object for whom the OTP is being generated
   * @return a {@code UserResponse} object containing the generated OTP
   */
  public UserResponse genrateOtp(User user) {
    log.debug("User successfully added.");

    UserAuthToken userAuthTokenOptional = userAuthTokenRepository.findByUserId(user.getId())
        .orElseThrow(() -> new InValidMobileNumberException("Invalid mobile number.", "9000102"));
    String otp = commonMethodUtil.genrate6DigitOtp();

    otpRepository.save(
        Otp.builder().otp(otp).userId(user.getId()).mobileNumber(user.getMobileNumber())
            .createdAt(LocalDateTime.now()).otpCreatedAt(LocalDateTime.now().plusMinutes(5))
            .build());

    return UserResponse.builder().otp(otp).token(userAuthTokenOptional.getToken()).build();
  }

  /**
   * Verifies the given OTP against the stored OTP for the current user. Throws an exception if the
   * OTP is invalid or expired.
   *
   * @param verifyOtpDto contains the OTP to be verified. Must not be {@code null}.
   * @return a {@link UserResponse} indicating that the OTP has been successfully verified.
   * @throws InvalidOtpException if the OTP is invalid or has expired.
   */
  public UserResponse verifyOtp(@NotNull VerifyOtpDto verifyOtpDto) {
    long userId = userProfile.getId();
    Otp otp = otpRepository.findByOtpAndUserId(verifyOtpDto.getOtp(), userId).orElseThrow(
        () -> new InvalidOtpException(LoggerMessageEnum.INVALID_OTP.getMessage(), "8000105"));
    if (!isOtpValidWithTimeDuration(otp)) {
      throw new InvalidOtpException(LoggerMessageEnum.INVALID_OTP.getMessage(), "8000105");
    }
    return UserResponse.builder().message("otp successfully verified.").build();
  }

  /**
   * Checks if the provided OTP is still valid based on its creation time. An OTP is considered
   * valid if it was created within the last 5 minutes.
   *
   * @param otp the OTP object containing the creation timestamp to be validated.
   * @return {@code true} if the OTP is valid (created within the last 5 minutes), {@code false}
   * otherwise.
   */
  private boolean isOtpValidWithTimeDuration(@NotNull Otp otp) {

    LocalDateTime otpCreatedDate = DateConverterUtils.convertLocalDateTimeFormat(
        otp.getOtpCreatedAt());

    log.info("OTP Created Date: {}", otpCreatedDate);
    LocalDateTime currentDateTime = DateConverterUtils.convertStringToLocalDateTime(
        DateConverterUtils.getCurrentDateTime());
    log.info("Current Date and Time: {}", currentDateTime);
    Duration timeElapsed = Duration.between(currentDateTime, otpCreatedDate);
    long minutesElapsed = timeElapsed.toMinutes();
    log.info("OTP Validity Duration: {} minutes", minutesElapsed);
    return minutesElapsed >= 0 && minutesElapsed <= 5;
  }
}
