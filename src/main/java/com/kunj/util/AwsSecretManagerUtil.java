package com.kunj.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunj.enums.AwsCredentialsStringConstant;
import com.kunj.exception.custome.InvalidAuthHeaderException;
import com.kunj.exception.custome.InvalidException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

@Component
@Slf4j
public class AwsSecretManagerUtil {

  private final SecretsManagerClient secretsManagerClient;

  public AwsSecretManagerUtil(@Value("${aws.region:ap-south-1}") String region
      , @Value("${access.key}") String accessKey,
      @Value("${secret.key}") String secretKey) {



    secretsManagerClient = SecretsManagerClient.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .build();
  }

  public Map<String, String> getSecretKeyByActiveEnvironment(String activeProfile) {
    log.info(" activeProfile for key {}:", activeProfile);
    String secretKeyJson = getSecretKey(
        activeProfile.concat(AwsCredentialsStringConstant.SECRET_KEY_POST_VALUE.getValue()));

    if (secretKeyJson == null) {
      log.error("Failed to retrieve secret for profile: {}", activeProfile);
      return Collections.emptyMap();
    }

    Type mapType = new TypeToken<Map<String, String>>() {
    }.getType();
    return new Gson().fromJson(secretKeyJson, mapType);
  }

  public String getSecretKey(String keyName) {
    log.info(" secret for key {}:", keyName);

    try {
      GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder()
          .secretId(keyName)
          .build();

      GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(
          secretValueRequest);
      return secretValueResponse.secretString();

    } catch (SecretsManagerException e) {
      log.error("Error retrieving secret for key {}: {}", keyName, e.getMessage());
      throw new InvalidException("Failed to retrieve secret from Secrets Manager", "402");
    }
  }

  public String getUpdatedSecretValueFromPropertyMap(Map<String, String> propertyMap,
      String propertiesValue) {

    if (StringUtils.hasLength(propertiesValue) && propertiesValue.startsWith("{asm}")) {
      String asmKey = propertiesValue.replace("{asm}", "");

      if (propertyMap.containsKey(asmKey)) {
        return propertyMap.get(asmKey);
      } else {
        log.error("Error while updating property key: {} and map: {}", asmKey, propertyMap);
        throw new InvalidAuthHeaderException("Invalid secret manager key: " + asmKey, "102335");
      }

    }
    return propertiesValue;
  }
}
