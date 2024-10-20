package com.kunj.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.kunj.config.PropertyConfig;
import com.kunj.enums.ConstantEnums;
import com.kunj.enums.LoggerMessageEnum;
import com.kunj.util.CommonMethodUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * The type Dynmo db auth service.
 */
@Component
@Slf4j
public class DynmoDbAuthService {

  private final PropertyConfig propertiesConfig;

  private final AmazonDynamoDB dynamoDB;

  /**
   * Instantiates a new Dynmo db auth service.
   *
   * @param propertiesConfig the properties config
   * @param dynamoDB         the dynamo db
   */
  public DynmoDbAuthService(PropertyConfig propertiesConfig, AmazonDynamoDB dynamoDB) {
    this.propertiesConfig = propertiesConfig;
    this.dynamoDB = dynamoDB;
  }

  /**
   * Reads data from a DynamoDB table by mobile number. This method queries a DynamoDB table to
   * check if any entry contains the specified mobile number. If such an entry exists, the method
   * returns true; otherwise, false.
   *
   * @param mobileNumber      the mobile number to search for in the DynamoDB table.
   * @param dynamoDbTableName the name of the DynamoDB table to query.
   * @return {@code true} if an entry with the specified mobile number exists in the table;
   * {@code false} otherwise.
   */
  public List<Map<String, AttributeValue>> readDataFromDynamoDbByMobileNumber(String mobileNumber,
      String dynamoDbTableName) {

    log.info(LoggerMessageEnum.READ_DATA_FROM_DYNAMODB_BY_MOBILE_NUMBER_LOGGER.getMessage(),
        mobileNumber, dynamoDbTableName);

    if (!com.kunj.util.StringUtils.hasLength(mobileNumber) && !com.kunj.util.StringUtils.hasLength(
        dynamoDbTableName)) {
      return Collections.emptyList();
    }
    QueryRequest queryRequest = findDataFromDynamoByMobileAndTableName(mobileNumber,
        dynamoDbTableName);

    List<Map<String, AttributeValue>> queryListMap = getItemListResult(queryRequest);
    log.info("Query result received with items{}", queryListMap);
    return Optional.ofNullable(getItemListResult(queryRequest)).orElse(Collections.emptyList());
  }

  private List<Map<String, AttributeValue>> getItemListResult(QueryRequest queryRequest) {
    log.info(LoggerMessageEnum.ITEM_LIST_RESULT_LOGGER.getMessage(), queryRequest);
    QueryResult queryResult;
    try {
      queryResult = dynamoDB.query(queryRequest);
      log.info(LoggerMessageEnum.ITEM_LIST_RESULT_LOGGER.getMessage(), queryRequest);
      return queryResult.getItems();
    } catch (Exception e) {
      log.error("Error occurred: {}", e.getMessage(), e);
      return Collections.emptyList();
    }
  }

  /**
   * Creates a {@link QueryRequest} to find data in a DynamoDB table by mobile number. This method
   * constructs a DynamoDB query request object that searches for items in a specified DynamoDB
   * table where the mobile number matches the given value.
   *
   * @param mobileNumber      the mobile number to search for in the DynamoDB table.
   * @param dynamoDbTableName the name of the DynamoDB table to query.
   * @return a {@link QueryRequest} object configured to search for items with the specified mobile
   * number.
   */
  private QueryRequest findDataFromDynamoByMobileAndTableName(String mobileNumber,
      String dynamoDbTableName) {

    log.info(LoggerMessageEnum.READ_DATA_FROM_DYNAMODB_BY_MOBILE_NUMBER_LOGGER.getMessage(),
        mobileNumber, dynamoDbTableName);

    Map<String, String> expressionAttributeNames = new HashMap<>();
    expressionAttributeNames.put(
        ConstantEnums.HAS_ATTRIBUTE_NAMES.getValue() + ConstantEnums.MOBILE_NUMBER.getValue(),
        ConstantEnums.MOBILE_NUMBER.getValue());

    Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
    expressionAttributeValues.put(ConstantEnums.DYNAMO_DB_PRIMARY_KEY_VALUE.getValue(),
        new AttributeValue().withS(mobileNumber));

    return new QueryRequest().withTableName(dynamoDbTableName)
        .withKeyConditionExpression(ConstantEnums.WITH_KEY_CONDITION_EXPRESSSION.getValue()
            + ConstantEnums.DYNAMO_DB_PRIMARY_KEY_VALUE.getValue())
        .withExpressionAttributeNames(expressionAttributeNames)
        .withExpressionAttributeValues(expressionAttributeValues);
  }

  /**
   * Persists the given data to an Amazon DynamoDB table. If the table does not exist, it will be
   * created first.
   *
   * <p>
   * This method performs the following steps:
   * <ol>
   * <li>Logs the data to be saved.</li>
   * <li>Creates an Amazon DynamoDB client using the provided AWS credentials and
   * region.</li>
   * <li>Checks if the table specified by {@code dynamoDbAuthTableName}
   * exists.</li>
   * <li>If the table exists and the data is not empty, it inserts the data into
   * the table.</li>
   * <li>If the table does not exist, it creates the table and then inserts the
   * data.</li>
   * </ol>
   *
   * @param dynamoDbData          a map of attribute names and values to be saved to the DynamoDB
   *                              table. The map should contain the data that needs to be persisted.
   *                              The keys represent the attribute names and the values represent
   *                              the attribute values.
   * @param dynamoDbAuthTableName the name of the DynamoDB table where the data will be saved. This
   *                              should be a non-empty string representing the table name.
   * @throws IllegalArgumentException if {@code dynamoDbData} is null or
   *                                  {@code dynamoDbAuthTableName} is null or empty.
   * @throws AmazonDynamoDBException  if there are issues interacting with DynamoDB (e.g.,
   *                                  permissions, network issues).
   */
  public void persistDataToDynamoDb(Map<String, AttributeValue> dynamoDbData,
      String dynamoDbAuthTableName) {

    log.info("save data in DynamoDb {}", dynamoDbData);
    AmazonDynamoDB amazonDynamoDBClient = AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials()))
        .withRegion(propertiesConfig.getRegion()).build();
    if (!CollectionUtils.isEmpty(dynamoDbData) && StringUtils.hasLength(dynamoDbAuthTableName)) {
        putItemDataToDynamoDb(dynamoDbData, amazonDynamoDBClient, dynamoDbAuthTableName);
        log.info("Data inserted successfully. {}", dynamoDbData);
    }
  }

  private void putItemDataToDynamoDb(Map<String, AttributeValue> dynamoDbData,
      AmazonDynamoDB amazonDynamoDBClient,
      String dynamoDbAuthTableName) {

    PutItemRequest putItemRequest = new PutItemRequest().withItem(dynamoDbData)
        .withTableName(dynamoDbAuthTableName);
    amazonDynamoDBClient.putItem(putItemRequest);
  }

  /**
   * Creates a new Amazon DynamoDB table with the specified name.
   *
   * <p>
   * This method sets up a table with the following characteristics:
   * <ul>
   * <li>The table name is specified by {@code dynamoDbAuthTableName}.</li>
   * <li>The table uses a single partition key, defined by
   * {@code ConstantEnums.MOBILE_NUMBER.getValue()}.</li>
   * <li>The partition key attribute is of type String
   * (ScalarAttributeType.S).</li>
   * <li>The table is provisioned with a read capacity of 5 units and a write
   * capacity of 5 units.</li>
   * </ul>
   *
   * <p>
   * The method assumes that the AWS credentials and region are properly
   * configured in the {@code awsCredentials()} method and
   * {@code propertiesConfig.getAwsRegion()} respectively.
   *
   * @param dynamoDbAuthTableName the name of the DynamoDB table to be created. This should be a
   *                              non-empty string representing the table name.
   * @throws IllegalArgumentException if {@code dynamoDbAuthTableName} is null or empty.
   * @throws AmazonDynamoDBException  if there are issues creating the table (e.g., table already
   *                                  exists, permissions issues).
   */
  private void createDynamoDbTable(String dynamoDbAuthTableName) {
    AmazonDynamoDB amazonDynamoDBClient = AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials()))
        .withRegion(propertiesConfig.getRegion()).build();

    CreateTableRequest request = new CreateTableRequest().withTableName(dynamoDbAuthTableName)
        .withKeySchema(new KeySchemaElement(ConstantEnums.MOBILE_NUMBER.getValue(), KeyType.HASH))
        .withAttributeDefinitions(
            new AttributeDefinition(ConstantEnums.MOBILE_NUMBER.getValue(), ScalarAttributeType.S))
        .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

    amazonDynamoDBClient.createTable(request);
  }

  private AWSCredentials awsCredentials() {
    return new BasicAWSCredentials(propertiesConfig.getAccessKey(),
        propertiesConfig.getSecretKey());
  }

  /**
   * Updates the token for a given mobile number in the specified DynamoDB table.
   *
   * <p>
   * This method performs the following steps:
   * <ol>
   * <li>Creates a key map to locate the item in the table based on the provided
   * mobile number.</li>
   * <li>Constructs an update request to set the new token value.</li>
   * <li>Uses the DynamoDB client to execute the update request.</li>
   * </ol>
   *
   * @param mobileNumber          the mobile number of the item to update. This value is used to
   *                              locate the item in the DynamoDB table.
   * @param token                 the new token value to set for the specified mobile number.
   * @param dynamoDbAuthTableName the name of the DynamoDB table where the item will be updated.
   *                              This should be a non-empty string representing the table name.
   * @throws IllegalArgumentException if {@code mobileNumber}, {@code token}, or
   *                                  {@code dynamoDbAuthTableName} is null or empty.
   * @throws AmazonDynamoDBException  if there are issues updating the item (e.g., permissions
   *                                  issues, network issues).
   */
  public void updateExpiredTokenInDynamoDb(String mobileNumber, String token,
      String dynamoDbAuthTableName) {

    if (!StringUtils.hasLength(mobileNumber) || !StringUtils.hasLength(token)
        || !StringUtils.hasLength(dynamoDbAuthTableName)) {
      throw new IllegalArgumentException(
          "Mobile number, token, and table name must not be null or empty.");
    }

    Map<String, AttributeValue> key = new HashMap<>();
    key.put(ConstantEnums.MOBILE_NUMBER.getValue(), new AttributeValue().withS(mobileNumber));

    Map<String, AttributeValueUpdate> attributeValueUpdateMap = new HashMap<>();
    CommonMethodUtil.setAttributeNamesAndValue(attributeValueUpdateMap, token);
    UpdateItemRequest updateItemRequest = new UpdateItemRequest().withTableName(
            dynamoDbAuthTableName).withKey(key)
        .withAttributeUpdates(attributeValueUpdateMap);
    try {
      dynamoDB.updateItem(updateItemRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}