package com.kunj.util;

import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.kunj.entity.User;
import com.kunj.entity.UserAuthToken;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * The type Common method util.
 */
@Component
public class CommonMethodUtil {

  /**
   * The Rnd.
   */
  Random rnd = new Random();

  /**
   * Map user data to map map.
   *
   * @param user          the user
   * @param userAuthToken the user auth token
   * @return the map
   */
  public static Map<String, AttributeValue> mapUserDataToMap(User user,
      UserAuthToken userAuthToken) {
    Map<String, AttributeValue> dynamoDbMapKey = new HashMap<>();

    dynamoDbMapKey.put("mobile_number", new AttributeValue(user.getMobileNumber()));
    dynamoDbMapKey.put("device_serial_number", new AttributeValue(user.getDeviceSerialNumber()));
    dynamoDbMapKey.put("device_token", new AttributeValue(user.getDeviceToken()));
    dynamoDbMapKey.put("auth_token", new AttributeValue(userAuthToken.getToken()));
    dynamoDbMapKey.put("token_issue_date",
        new AttributeValue(DateConverterUtils.getCurrentDateTime()));
    dynamoDbMapKey.put("token_expire_date",
        new AttributeValue(DateConverterUtils.getExpireTokenWith12Hrs()));
    dynamoDbMapKey.put("user_id", new AttributeValue(user.getId().toString()));
    dynamoDbMapKey.put("role",
        new AttributeValue(user.getRole()));  // Corrected typo from "onwer" to "owner"
    return dynamoDbMapKey;
  }

  /**
   * Sets attribute names and value.
   *
   * @param attributeValueUpdateMap the attribute value update map
   * @param token                   the token
   */
  public static void setAttributeNamesAndValue(
      Map<String, AttributeValueUpdate> attributeValueUpdateMap, String token) {
    attributeValueUpdateMap.put("token",
        new AttributeValueUpdate().withValue(new AttributeValue().withS(token))
            .withAction(AttributeAction.PUT));
    attributeValueUpdateMap.put("token_issue_date",
        new AttributeValueUpdate().withValue(
                new AttributeValue().withS(DateConverterUtils.getCurrentDateTime()))
            .withAction(AttributeAction.PUT));
    attributeValueUpdateMap.put("token_expire_date",
        new AttributeValueUpdate().withValue(
                new AttributeValue().withS(DateConverterUtils.getExpireTokenWith12Hrs()))
            .withAction(AttributeAction.PUT));
  }


  /**
   * Create auth token user auth token.
   *
   * @param token the token
   * @param user  the user
   * @return the user auth token
   */
  public static UserAuthToken createAuthToken(String token, User user) {
    UserAuthToken userAuthToken = new UserAuthToken();
    userAuthToken.setTokenExpireDate(DateConverterUtils.getExpireTokenWith12Hrs());
    userAuthToken.setTokenIssueDate(DateConverterUtils.getCurrentDateTime());
    userAuthToken.setToken(token);
    userAuthToken.setUser(user);
    return userAuthToken;
  }

  /**
   * Genrate 6 digit otp string.
   *
   * @return the string
   */
  public String genrate6DigitOtp() {
    int otp = rnd.nextInt(999999);
    return String.format("%06d", otp);
  }
}
