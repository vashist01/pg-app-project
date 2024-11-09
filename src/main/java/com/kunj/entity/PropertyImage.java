package com.kunj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property_images")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PropertyImage  {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private long id;
  @Column(name = "image_url")
  private String propertyImageUrl;

  @ManyToOne
  @JoinColumn(name = "property_id")
  private OwnerProperty ownerProperty;

  @Column(name = "user_id")
  private long userId;

  @Column(name = "image_name")
  private String imageName;
}
