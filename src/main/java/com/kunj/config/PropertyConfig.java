package com.kunj.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The type Property config.
 */
@Configuration
@Data
public class PropertyConfig {

  private final String verifyOtpTiemDuration;

  private final String dnymoDbTableName;
  private final String region;
  private final String accessKey;
  private final String jwtSecretKey;
  private final String secretKey;
  private final long jwtExpiration;
//  private final AwsSecretManagerUtil awsSecretManagerUtil;
  private String activeProfile;


  /**
   * Instantiates a new Property config.
   *
   * @param verifyOtpTimeDuration the verify otp time duration
   * @param DynamoDBTableName     the dynamo db table name
   * @param region                the region
   * @param accessKey             the access key
   * @param jwtSecretKey          the jwt secret key
   * @param secretKey             the secret key
   * @param jwtExpiration         the jwt expiration
   * @param activeProfile         the active profile
   * @param awsSecretManagerUtil1 the aws secret manager util 1
   */
  public PropertyConfig(@Value("${verify.otp.time.duration}") final String verifyOtpTimeDuration,
      @Value("${dynamodb.table.name}") String DynamoDBTableName,
      @Value("${region}") String region,
      @Value("${access.key}") String accessKey,
      @Value("${security.jwt.secret-key}") String jwtSecretKey,
      @Value("${secret.key}") String secretKey,
      @Value("${security.jwt.expiration-time}") long jwtExpiration,
      @Value("${spring.profiles.active}") String activeProfile) {

   // this.awsSecretManagerUtil = awsSecretManagerUtil1;
    this.verifyOtpTiemDuration = verifyOtpTimeDuration;
//    Map<String, String> asmProperties = awsSecretManagerUtil1.getSecretByEnvironment(
//        activeProfile);
    this.dnymoDbTableName = DynamoDBTableName;
    // this.region = awsSecretManagerUtil1.getUpdatedSecretValueFromPropertyMap(asmProperties,secretKey);
    // this.accessKey =awsSecretManagerUtil1.getUpdatedSecretValueFromPropertyMap(asmProperties,accessKey);
    this.region = region;
    this.accessKey = accessKey;
    this.jwtSecretKey = jwtSecretKey;
    this.secretKey = secretKey;
    this.jwtExpiration = jwtExpiration;
  }
}
