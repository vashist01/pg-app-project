package com.kunj.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Validate auth token request.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateAuthTokenRequest {

  private String deviceSerialNumber;
  private String dynamoTableName;
  private String token;
}