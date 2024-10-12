package com.kunj.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * The type Property request dto.
 */
@Data
@ToString
public class PropertyRequestDTO {

  @JsonProperty("flat_name")
  private String flatName;
  private String location;
  private String address;
  @JsonProperty("owner_name")
  private String ownerName;
  @JsonProperty("property_type")
  private String propertyCategory;
  @JsonProperty("rent_amount")
  private String rentAmount;
  private String negotiation;
  private String facility;
  private String pinCode;
  private String area;
  private String city;
  private String state;
  private String latitude;
  private String longitude;
  private String security;
  @JsonProperty("category_id")
  private String categoryId;
}
