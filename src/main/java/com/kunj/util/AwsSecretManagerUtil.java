package com.kunj.util;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.google.gson.Gson;
import com.kunj.exception.custome.InvalidAuthHeaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

import java.util.Map;

@Component
@Slf4j
public class AwsSecretManagerUtil {

  private final String POST_KEY_WITH_ENV = "/secret-key";

  private   SecretsManagerClient secretsManagerClient;

  public AwsSecretManagerUtil(@Value("${aws.region:ap-south-1}") String region) {
    secretsManagerClient = SecretsManagerClient.builder()
        .region(Region.of("ap-south-1"))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create("AKIAVL7I4P37RUPAI3MY", "kCO1gtTEeyGE+ubXOBmUH+wXpnaOmWZmwinD1jyT")))
        .build();
  }

  public Map<String, String> getSecretKeyByActiveEnvironment(String activeProfile) {
    String secretJson = getSecretKey(activeProfile.concat(POST_KEY_WITH_ENV));
    if (secretJson == null) {
      log.error("Failed to retrieve secret for profile: {}", activeProfile);
      return null;
    }
    return new Gson().fromJson(secretJson, Map.class);
  }

  public String getSecretKey(String keyName) {
    try {
      GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder()
          .secretId(keyName)
          .build();

      GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(secretValueRequest);
      return secretValueResponse.secretString();
    } catch (SecretsManagerException e) {
      log.error("Error retrieving secret for key {}: {}", keyName, e.getMessage());
      throw new RuntimeException("Failed to retrieve secret from Secrets Manager", e);
    }
  }

  public String getUpdatedSecretValueFromPropertyMap(Map<String, String> propertyMap, String propertiesValue) {
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
