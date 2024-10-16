package com.kunj.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Property response dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponseDTO {

  @JsonProperty("flat_number")
  private String flatNumber;

  private String location;
  private String address;

  @JsonProperty("owner_name")
  private String ownerName;

  @JsonProperty("property_type")
  private String propertyType;

  @JsonProperty("total_view")
  private int totalView;
  @JsonProperty("rent_amount")
  private String rentAmount;
  @JsonProperty(defaultValue = "y")
  private String negotiation;
  private String facility;
  @JsonProperty("pin_code")
  private String pinCode;
  private String area;
  private String city;
  private String state;
  private String latitude;
  private String longitude;
  private String security;
}
