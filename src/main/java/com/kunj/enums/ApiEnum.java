package com.kunj.enums;


public enum ApiEnum {

  AVI_VERSION("/api/v1/"),;
  public static final String GET_ALL_PROPERTY_CATEGORY ="/property-category";
  public static final String GET_PROPERTY = "/properties";

  public static final String API_VERSION = "/api/v_1";

  public static final String USER_REGISTER = "/login";

  public static final String VERIFY_OTP = "/verify-otp";

  public static final String API_ADD_PROPERTY = "/add-property";

  public static final String OTP_PETTERN_VALIDATION = "[0-9a-zA-Z]*[0-9]+[0-9a-zA-Z]*";

  public static final String CREATE_PROFILE ="/create-user";
  public  static final String UPDATE_PROFILE ="/update-profile";
  public static final String LOGIN ="/login";
  public static final String  UPLOAD_PROFILE_IMAGE ="/upload-profile-image";

  private final String apiName;

  ApiEnum(String apiName) {
    this.apiName = apiName;
  }

  public String getApiName() {
    return apiName;
  }

  @Override
  public String toString() {
    return apiName;
  }
}
