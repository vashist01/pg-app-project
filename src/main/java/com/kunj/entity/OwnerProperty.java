package com.kunj.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
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

  @JoinColumn(name = "flat_number")
  private String flatNumber;

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

  private String sector;

  @OneToMany(mappedBy = "ownerProperty", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<PropertyImage> propertyImages;
}
