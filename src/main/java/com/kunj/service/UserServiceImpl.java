package com.kunj.service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kunj.config.PropertyConfig;
import com.kunj.dto.request.UserDto;
import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.request.VerifyOtpDto;
import com.kunj.dto.response.UserResponse;
import com.kunj.entity.Otp;
import com.kunj.entity.User;
import com.kunj.entity.UserAuthToken;
import com.kunj.enums.ValidationEnum;
import com.kunj.exception.NoDataFoundException;
import com.kunj.exception.custome.InValidMobileNumberException;
import com.kunj.repository.OtpRepository;
import com.kunj.repository.UserAuthTokenRepository;
import com.kunj.repository.UserProfileRepository;
import com.kunj.repository.UserRepository;
import com.kunj.util.CommonMethodUtil;
import com.kunj.util.ConvertorUtil;
import com.kunj.util.DateConverterUtils;
import com.kunj.util.components.TokenValidatorComponent;
import com.kunj.util.components.UserProfileRequestScop;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * The type User service.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;


  private final UserAuthTokenRepository userAuthTokenRepository;
  private final ConvertorUtil convertorUtil;
  private final PropertyConfig propertyConfig;
  private final DynmoDbAuthService dynamoDbAuthService;
  private final TokenValidatorComponent tokenValidatorComponent;
  private final JwtTokenAuthService jwtTokenAuthService;
  private final UserProfileRequestScop userProfileRequestScop;
  private final OtpService otpService;
  private final OtpRepository otpRepository;
  private final PropertyService propertyService;
  private final UserProfileService userProfileService;
  private Date TOKEN_EXPIRE_DATE = new Date(System.currentTimeMillis() + 1000 * 60 * 10);

  /**
   * Instantiates a new User service.
   *
   * @param userRepository          the user repository
   * @param otpRepository           the otp repository
   * @param commonMethodUtil        the common method util
   * @param userAuthTokenRepository the user auth token repository
   * @param convertorUtil           the convertor util
   * @param propertyConfig          the property config
   * @param dynamoDbAuthService     the dynamo db auth service
   * @param tokenValidatorComponent the token validator component
   * @param jwtTokenAuthService1    the jwt token auth service 1
   * @param userProfileRequestScop             the user profile
   * @param otpService              the otp service
   * @param otpRepository1          the otp repository 1
   */
  public UserServiceImpl(UserRepository userRepository, OtpRepository otpRepository,
      CommonMethodUtil commonMethodUtil, UserAuthTokenRepository userAuthTokenRepository,
      ConvertorUtil convertorUtil, final PropertyConfig propertyConfig,
      DynmoDbAuthService dynamoDbAuthService,
      TokenValidatorComponent tokenValidatorComponent,
      JwtTokenAuthService jwtTokenAuthService1, UserProfileRequestScop userProfileRequestScop, OtpService otpService,
      OtpRepository otpRepository1, PropertyService propertyService,
      UserProfileRepository userProfileRepository, UserProfileService userProfileService) {
    this.userRepository = userRepository;

    this.userAuthTokenRepository = userAuthTokenRepository;
    this.convertorUtil = convertorUtil;
    this.propertyConfig = propertyConfig;
    this.dynamoDbAuthService = dynamoDbAuthService;
    this.tokenValidatorComponent = tokenValidatorComponent;

    this.jwtTokenAuthService = jwtTokenAuthService1;
    this.userProfileRequestScop = userProfileRequestScop;
    this.otpService = otpService;
    this.otpRepository = otpRepository1;
    this.propertyService = propertyService;
    this.userProfileService = userProfileService;
  }

  /**
   * Handles the login process for a user based on the provided {@code UserDto}.
   *
   * <p>
   * This method converts the CUID number in the {@code UserDto} to a mobile number, checks if a
   * user with this mobile number exists in the repository, and either creates a new user or
   * retrieves the existing user. It then generates a 6-digit OTP for the user and returns a
   * {@code UserResponse} containing the generated OTP.
   *
   * @param userDto the {@code UserDto} containing the user's login information
   * @return a {@code UserResponse} object containing the generated OTP
   */
  @Override
  public UserResponse login(UserDto userDto) {
    log.info("User login method is being executed for mobile number: {}", userDto);

    Pair<String,String> mobileNumberAndDynamoDbTable = getDynmoDbTableAndMobileNumber(userDto);
    String mobileNumber = mobileNumberAndDynamoDbTable.getLeft();
    String dynamoDbAuthTableName = mobileNumberAndDynamoDbTable.getRight();


    List<Map<String, AttributeValue>> readDynamoDbData = dynamoDbAuthService
        .readDataFromDynamoDbByMobileNumber(mobileNumberAndDynamoDbTable.getLeft(),
            mobileNumberAndDynamoDbTable.getRight());
  
    Optional<User> userOptional = userRepository.findByMobileNumber(mobileNumber); 
    if (userOptional.isEmpty()) {
        throw new InValidMobileNumberException("Invalid mobile number. Please check the number you entered or register if you don't have an account.","400");
    } 
    String token = jwtTokenAuthService.genrateToken(mobileNumber, TOKEN_EXPIRE_DATE);
    log.info("User login method is being executed for mobile number: {}", userDto);
      User user = userRepository.findByMobileNumber(userDto.getMobileNumber()).orElseThrow(() -> new InValidMobileNumberException("Invalid User Mobile Number","100200"));
      UserAuthToken userAuthToken = CommonMethodUtil.createAuthToken(token, user,userAuthTokenRepository);

      userAuthTokenRepository.save(userAuthToken);
      Map<String, AttributeValue> dynamoDbData = CommonMethodUtil.mapUserDataToMap(user,
          userAuthToken);
      log.info("User login method is being executed and persistDataToDynamoDb :  {}",
          dynamoDbAuthTableName);
      dynamoDbAuthService.persistDataToDynamoDb(dynamoDbData, dynamoDbAuthTableName);
      return otpService.genrateOtp(user);
  }


  private Pair<String, String> getDynmoDbTableAndMobileNumber(UserDto userDto) {
    //String mobileNumber = convertorUtil.convertCUIDNumberToMobile(userDto.getMobileNumber());
    String dynamoDbAuthTableName = propertyConfig.getDnymoDbTableName();
    return Pair.of(userDto.getMobileNumber(),dynamoDbAuthTableName);
  }

  private void updateTokenIfExpired(String dynamoDbAuthTableName,
      List<Map<String, AttributeValue>> readDynamoDbData, String mobileNumber, String token,
      Optional<User> userOptional) {

    log.info(
        "Token is invalid and DynamoDB data is not empty. Updating expired token in DynamoDB.");

    if (CollectionUtils.isEmpty(readDynamoDbData)) {

      dynamoDbAuthService.updateExpiredTokenInDynamoDb(mobileNumber, token, dynamoDbAuthTableName);
      Optional<UserAuthToken> userAuthTokenOptional = userAuthTokenRepository.findById(
          userOptional.get().getId());

      if (userAuthTokenOptional.isPresent()) {
        UserAuthToken userAuthToken = userAuthTokenOptional.get();
        userAuthToken.setToken(token);
        userAuthToken.setTokenExpireDate(DateConverterUtils.getExpireTokenWith12Hrs());
        userAuthToken.setTokenIssueDate(DateConverterUtils.getCurrentDateTime());
        userAuthTokenRepository.save(userAuthToken);
        log.info("Token updated successfully for user with ID: {}", userAuthToken.getId());
      }
    }

  }


  @Override
  public UserResponse verifyOtp(VerifyOtpDto verifyOtpDto) {
    String verifyOtp = verifyOtpDto.getOtp();
    String mobileNumber = convertorUtil.convertCUIDNumberToMobile(verifyOtpDto.getMobileNumber());

    Optional<Otp> optionalOtp = otpRepository.findByOtpAndMobileNumber(verifyOtp, mobileNumber);
    if (optionalOtp.isEmpty()) {
      throw new NoDataFoundException("Otp is not matched with mobile number", "8001001");
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
    throw new NoDataFoundException("Otp is not matched with mobile number", "8001001");
  }
}
