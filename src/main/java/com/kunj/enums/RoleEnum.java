package com.kunj.enums;

public enum RoleEnum {
  OWNER("Owner"),
  TENANT("Tenant");

  private final String roleType;


  RoleEnum(String roleType) {
    this.roleType = roleType;
  }


  public String getRoleType() {
    return roleType;
  }
}
