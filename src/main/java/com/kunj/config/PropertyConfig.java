package com.kunj.config;

import com.kunj.util.AwsSecretManagerUtil;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The type Property config.
 */
@Configuration
@Data
@Slf4j
public class PropertyConfig {

  private final String verifyOtpTiemDuration;

  private final String dnymoDbTableName;
  private final String region;
  private final String jwtSecretKey;
  private final String accessKey;
  private final String secretKey;
  private final long jwtExpiration;
  private final AwsSecretManagerUtil awsSecretManagerUtil;
  private String activeProfile;
  private final String s3ProfileImageLocation;


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
   */
  public PropertyConfig(@Value("${verify.otp.time.duration}") final String verifyOtpTimeDuration,
      @Value("${dynamodb.table.name}") String DynamoDBTableName,
      @Value("${region}") String region,
      @Value("${access.key}") String accessKey,
      @Value("${security.jwt.secret-key}") String jwtSecretKey,
      @Value("${secret.key}") String secretKey,
      @Value("${security.jwt.expiration-time}") long jwtExpiration,
      @Value("${spring.profiles.active}") String activeProfile,
      AwsSecretManagerUtil awsSecretManagerUtil,
      @Value("${s3.profile.image.location}") String s3ProfileImageLocation) {

    this.verifyOtpTiemDuration = verifyOtpTimeDuration;

    this.awsSecretManagerUtil = awsSecretManagerUtil;
    this.s3ProfileImageLocation = s3ProfileImageLocation;
    log.info(" secretKey for key {}:", secretKey);
    log.info(" accessKey for key {}:", accessKey);
    Map<String, String> asmProperties = awsSecretManagerUtil.getSecretKeyByActiveEnvironment(
        activeProfile);
    this.dnymoDbTableName = DynamoDBTableName;

    this.secretKey = awsSecretManagerUtil.getUpdatedSecretValueFromPropertyMap(asmProperties,
        secretKey);
    this.accessKey = awsSecretManagerUtil.getUpdatedSecretValueFromPropertyMap(asmProperties,
        accessKey);

    this.jwtSecretKey = jwtSecretKey;
    this.region = region;
    this.jwtExpiration = jwtExpiration;
  }
}
