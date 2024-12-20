package com.kunj.enums;


import lombok.Getter;

@Getter
public enum ApiEnum {

  AVI_VERSION("/api/v1/");
  public static final String   DELETE_OWNER_PROPERTY ="/delete-property";
  public static final String GET_ALL_PROPERTY_CATEGORY = "/property-category";
  public static final String GET_PROPERTY = "/properties";
  public static final String API_VERSION = "/api/v_1";

  public static final String USER_LOGIN = "/login";

  public static final String VERIFY_OTP = "/verify-otp";

  public static final String API_ADD_PROPERTY = "/add-property";

  public static final String OTP_PATTERN_VALIDATION = "[0-9a-zA-Z]*[0-9]+[0-9a-zA-Z]*";

  public static final String REGISTER_USER = "/create-user";
  public static final String UPDATE_PROFILE = "/update-profile";
  public static final String UPLOAD_PROFILE_IMAGE = "/upload-profile-image";
  public static final String PROFILE_IMAGE = "/profile-image";
  public static final String GET_PROPERTY_DETAIL_BY_ID = "/property-details/{propertyId}";
  public static final String GET_PROPERTY_BY_LOCATION = "/property-by-location";
  public static final String UPLOAD_PROPERTY_IMAGE = "/upload-property-image";

  private final String apiName;

  ApiEnum(String apiName) {
    this.apiName = apiName;
  }

  @Override
  public String toString() {
    return apiName;
  }
}
