package com.kunj.util.components;//package com.kunj.util.components;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
//import com.google.gson.Gson;
//import com.kunj.enums.ConstantEnums;
//import com.kunj.enums.ValidationEnum;
//import com.kunj.exception.custome.ValidationException;
//import java.lang.reflect.Type;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.TypeToken;
//import org.springframework.stereotype.Component;
//import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
//
///**
// * Utility class for interacting with AWS Secrets Manager. Provides methods to retrieve secrets and
// * update properties based on secrets.
// */
//@Component
//@Slf4j
//public class AwsSecretManagerUtil {
//
//  private static AwsSecretManagerUtil awsSecretManagerUtil;
//
//  /**
//   * The Region.
//   */
//  Region region = Region.of("ap-south-1");
//
//  /**
//   * The Secrets manager client.
//   */
//// Create a Secrets Manager client
//  SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
//      .region(region)
//      .build();
//
//  /**
//   * Private constructor to initialize the SecretsManagerClient.
//   */
//  private AwsSecretManagerUtil() {
//
//    try {
//      AWSCredentialsProvider provider = new DefaultAWSCredentialsProviderChain();
//      AWSCredentials credentials = provider.getCredentials();
//      if (credentials != null) {
//        log.info("Credentials Key: " + credentials.getAWSAccessKeyId());
//        log.info("Credentials secret: " + credentials.getAWSSecretKey());
//      }
//    } catch (Exception e) {
//      log.info("Exception in credentials cause:" + e.getCause() + ";message: " + e.getMessage()
//          + ";stack: " + e.getStackTrace());
//    }
//    secretsManagerClient = SecretsManagerClient.builder()
//        .region(Region.AP_SOUTH_1)
//        .credentialsProvider(DefaultCredentialsProvider.create())
//        .build();
//    log.info("Initialized SecretsManagerClient with region {}", Region.AP_SOUTH_1);
//  }
//
//  /**
//   * Retrieves a secret from AWS Secrets Manager based on the application environment.
//   *
//   * @param applicationEnviroment the application environment for which the secret is retrieved.
//   * @return a map of secret key-value pairs.
//   */
//  public Map<String, String> getSecretByEnvironment(String applicationEnviroment) {
//    log.info("Fetching secret for environment: {}", applicationEnviroment);
//    String secretJson = getSecretByKeyName(applicationEnviroment);
//
//    Type type = new TypeToken<Map<String, String>>() {
//    }.getType();
//    Map<String, String> secretMap = new Gson().fromJson(secretJson, type);
//
//    log.debug("Fetched secret map: {}", secretMap);
//    return secretMap;
//  }
//
//  /**
//   * Retrieves a secret string from AWS Secrets Manager using the provided key name.
//   *
//   * @param applicationEnvironment the key name of the secret.
//   * @return the secret value as a string.
//   */
//  private String getSecretByKeyName(String applicationEnvironment) {
//    log.info("Retrieving secret value for key: {}", applicationEnvironment);
//
//    GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
//        .secretId(applicationEnvironment)
//        .build();
//    GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(
//        getSecretValueRequest);
//
//    String secretString = getSecretValueResponse.secretString();
//    log.debug("Retrieved secret value for key {}: {}", applicationEnvironment, secretString);
//    return secretString;
//  }
//
//  /**
//   * Updates the secret value from a property map based on the provided secret manager value.
//   *
//   * @param propertiesMap      the map containing property values.
//   * @param secretManagerValue the value from the secret manager to be updated.
//   * @return the updated secret value.
//   * @throws ValidationException if the secret manager key is invalid or not found in the properties
//   *                             map.
//   */
//  public String getUpdatedSecretValueFromPropertyMap(Map<String, String> propertiesMap,
//      String secretManagerValue) {
//    if (secretManagerValue != null && secretManagerValue.startsWith(
//        ConstantEnums.PROPERTIES_PREFIX_FOR_SECRET_MANAGER.getValue())) {
//      String awsSecretManagerKey = secretManagerValue.replace(
//          ConstantEnums.PROPERTIES_PREFIX_FOR_SECRET_MANAGER.getValue(), "");
//
//      if (propertiesMap.containsKey(awsSecretManagerKey)) {
//        String updatedValue = propertiesMap.get(awsSecretManagerKey);
//        log.info("Updated secret value for key '{}': {}", awsSecretManagerKey, updatedValue);
//        return updatedValue;
//      }
//
//      log.error("Secret manager key '{}' not found in properties map: {}", awsSecretManagerKey,
//          propertiesMap);
//      throw new ValidationException(ValidationEnum.INVALID_AWS_SECRET_MANAGER_KEY.getMessage(),
//          "800101");
//    }
//
//    log.debug(
//        "Secret manager value is not valid or does not start with prefix. Returning original value: {}",
//        secretManagerValue);
//    return secretManagerValue;
//  }
//}
