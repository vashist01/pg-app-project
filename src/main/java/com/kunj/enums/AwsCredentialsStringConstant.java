package com.kunj.enums;

import lombok.Getter;

@Getter
public enum AwsCredentialsStringConstant {
  SECRET_KEY_POST_VALUE("/secret-key");

  private final String value;

  AwsCredentialsStringConstant(String value) {
    this.value = value;
  }

}
