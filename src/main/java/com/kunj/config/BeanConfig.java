package com.kunj.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Bean config.
 */
@Configuration
public class BeanConfig {

  private final PropertyConfig propertyConfig;

  /**
   * Instantiates a new Bean config.
   *
   * @param propertyConfig the property config
   */
  public BeanConfig(PropertyConfig propertyConfig) {
    this.propertyConfig = propertyConfig;
  }

  /**
   * Aws secrets manager aws secrets manager.
   *
   * @return the aws secrets manager
   */
  @Bean
  public AWSSecretsManager awsSecretsManager() {
    return AWSSecretsManagerClientBuilder.standard().withRegion(propertyConfig.getRegion()).build();
  }

  /**
   * Dynamo db amazon dynamo db.
   *
   * @return the amazon dynamo db
   */
  @Bean
  public AmazonDynamoDB dynamoDB() {
    return AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials()))
        .withRegion(propertyConfig.getRegion()).build();
  }

  /**
   * Aws credentials aws credentials.
   *
   * @return the aws credentials
   */
  public AWSCredentials awsCredentials() {
    return new BasicAWSCredentials(propertyConfig.getAccessKey(), propertyConfig.getSecretKey());
  }

  @Bean
  public AmazonS3 s3client() {
    return AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials()))
        .withRegion(propertyConfig.getRegion())
        .build();
  }
}