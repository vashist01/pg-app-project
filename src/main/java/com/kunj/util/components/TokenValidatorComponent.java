package com.kunj.util.components;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.kunj.enums.ConstantEnums;
import com.kunj.util.DateConverterUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * The type Token validator component.
 */
@Component
@Slf4j
public class TokenValidatorComponent {

  private final DateConverterUtils dateConverterUtils;

  /**
   * Instantiates a new Token validator component.
   *
   * @param dateConverterUtils the date converter utils
   */
  public TokenValidatorComponent(DateConverterUtils dateConverterUtils) {
    this.dateConverterUtils = dateConverterUtils;
  }

  /**
   * Validate token expire with dymaodb boolean.
   *
   * @param queryListMap the query list map
   * @return the boolean
   */
  public boolean validateTokenExpireWithDymaodb(List<Map<String, AttributeValue>> queryListMap) {
    if (CollectionUtils.isEmpty(queryListMap)) {
      return false;
    }

    String tokenExpiredDate = claimsTokenData(queryListMap,
        ConstantEnums.TOKEN_EXPIRE_DATE.getValue());
    return dateConverterUtils.isTokenValid(tokenExpiredDate);
  }

  /**
   * Claims token data string.
   *
   * @param queryListMap    the query list map
   * @param attributeValues the attribute values
   * @return the string
   */
  public String claimsTokenData(List<Map<String, AttributeValue>> queryListMap,
      String attributeValues) {

    for (Map<String, AttributeValue> mapList : queryListMap) {
      AttributeValue attributeValue = mapList.get(attributeValues);
      if (Objects.nonNull(attributeValue)) {
        return attributeValue.getS();
      }
    }
    return null;
  }
}
