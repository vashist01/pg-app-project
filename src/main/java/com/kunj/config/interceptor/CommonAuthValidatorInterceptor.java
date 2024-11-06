package com.kunj.config.interceptor;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kunj.ResponseMessageConstant;
import com.kunj.config.PropertyConfig;
import com.kunj.dto.mapper.UserData;
import com.kunj.enums.ConstantEnums;
import com.kunj.exception.custome.InvalidAuthHeaderException;
import com.kunj.service.DynmoDbAuthService;
import com.kunj.service.JwtTokenAuthService;
import com.kunj.util.components.TokenValidatorComponent;
import com.kunj.util.components.UserProfileRequestScop;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * The type Common auth validator interceptor.
 */
@Component
@Slf4j
public class CommonAuthValidatorInterceptor implements HandlerInterceptor {

  private final JwtTokenAuthService jwtTokenAuthService;
  private final PropertyConfig propertiesConfig;
  private final TokenValidatorComponent tokenValidatorComponent;
  private final DynmoDbAuthService dynmoDbAuthService;
  private final UserProfileRequestScop userProfile;


  /**
   * Instantiates a new Common auth validator interceptor.
   *
   * @param jwtTokenAuthService     the jwt token auth service
   * @param propertiesConfig        the properties config
   * @param tokenValidatorComponent the token validator component
   * @param dynmoDbAuthService      the dynmo db auth service
   * @param userProfile             the user profile
   */
  public CommonAuthValidatorInterceptor(JwtTokenAuthService jwtTokenAuthService,
      PropertyConfig propertiesConfig,
      TokenValidatorComponent tokenValidatorComponent,
      DynmoDbAuthService dynmoDbAuthService, UserProfileRequestScop userProfile) {
    this.jwtTokenAuthService = jwtTokenAuthService;
    this.propertiesConfig = propertiesConfig;
    this.tokenValidatorComponent = tokenValidatorComponent;
    this.dynmoDbAuthService = dynmoDbAuthService;

    this.userProfile = userProfile;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    log.info("Processing request");

    String authHeader = request.getHeader(ConstantEnums.AUTHORIZATION.getValue());
    String deviceSerialNumber = request.getHeader(ConstantEnums.DEVICE_SERIAL.getValue());

    validateAuthHeader(authHeader);

    if (StringUtils.hasLength(authHeader) && authHeader.startsWith(
        ConstantEnums.BEARER.getValue())) {
      String token = extractToken(authHeader);
      if (StringUtils.hasLength(token) && StringUtils.hasLength(deviceSerialNumber)) {
        return processToken(token, response);
      }
    }

    log.warn("Authorization header is missing or invalid");
    throw new InvalidAuthHeaderException(ResponseMessageConstant.BAD_CREDENTIALS, ResponseMessageConstant.ERROR_CODE);
  }

  private void validateAuthHeader(String authHeader) throws InvalidAuthHeaderException {
    if (!StringUtils.hasLength(authHeader) || !authHeader.startsWith(
        ConstantEnums.BEARER.getValue())) {
      log.warn("Authorization header is missing or empty");
      throw new InvalidAuthHeaderException(ResponseMessageConstant.BAD_CREDENTIALS, ResponseMessageConstant.ERROR_CODE);
    }
  }

  private String extractToken(String authHeader) {
    return authHeader.substring(ConstantEnums.BEARER.getValue().length()).trim();
  }

  private boolean processToken(String token, HttpServletResponse response) throws Exception {

  String mobileNumber = jwtTokenAuthService.claimsMobileNumberFromToken(token);
    log.info("token successfully processed .......... ");
    if (mobileNumber == null) {
      log.error("Invalid authentication header provided");
      throw new InvalidAuthHeaderException("Invalid authentication header provided.", ResponseMessageConstant.ERROR_CODE);
    }

    List<Map<String, AttributeValue>> readDynamoDbData = dynmoDbAuthService.readDataFromDynamoDbByMobileNumber(
        mobileNumber,
        propertiesConfig.getDnymoDbTableName());

    if (CollectionUtils.isEmpty(readDynamoDbData)) {
      log.error("DynamoDB data is empty for mobile number: {}", mobileNumber);
      throw new InvalidAuthHeaderException("Invalid authentication header provided.", ResponseMessageConstant.ERROR_CODE);
    }
    UserData user = readUserDataFromDynamoDb(readDynamoDbData);
    BeanUtils.copyProperties(user, userProfile);
    String dynamoDbMobileNumber = tokenValidatorComponent.claimsTokenData(readDynamoDbData,
        ConstantEnums.MOBILE_NUMBER.getValue());
    if (!mobileNumber.equalsIgnoreCase(dynamoDbMobileNumber)) {
      log.error("Mobile number mismatch: expected {}, found {}", dynamoDbMobileNumber,
          mobileNumber);
      throw new InvalidAuthHeaderException("Bad Credentials.", ResponseMessageConstant.ERROR_CODE);
    }

    boolean isTokenValid = tokenValidatorComponent.validateTokenExpireWithDymaodb(readDynamoDbData);
    if (!isTokenValid) {
      log.error("Token expired or invalid");
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
      return false;
    }
    return true;
  }

  private UserData readUserDataFromDynamoDb(List<Map<String, AttributeValue>> readDynamoDbData) {
    UserData userData = new UserData();
     readDynamoDbData.forEach(attributeValueMap -> {
       userData.setId(Long.valueOf(attributeValueMap.get("user_id").getS()));
       userData.setMobileNumber(attributeValueMap.get(ConstantEnums.MOBILE_NUMBER.getValue()).getS());
       userData.setRole(attributeValueMap.get("role").getS());
     });
    return userData;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    log.info("Request completed");
    if (ex != null) {
      log.error("Exception occurred during request processing: ", ex);
    }
  }
}
