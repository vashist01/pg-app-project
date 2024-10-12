package com.kunj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Owner property.
 */
@Entity
@Table(name = "owner_property")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OwnerProperty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String location;
  private String address;

  @Column(name = "owner_name")
  private String ownerName;


  @Column(name = "property_type")
  private String propertyType;

  @Column(name = "user_id")
  private long userId;

  @Column(name = "rent_amount")
  private String rentAmount;

  @JoinColumn(columnDefinition = "y")
  private String negotiation;

  private String facility;

  @Column(name = "pin_code")
  private String pinCode;

  private String area;

  private String city;

  private String state;

  @Column(name = "total_view")
  private int totalView;

  private String latitude;

  private String longitude;

  private String security;

  @Column(name = "category_id")
  private long categoryId;
}
