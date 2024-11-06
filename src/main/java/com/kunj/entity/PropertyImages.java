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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property_images")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyImages {

  @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "image_url")
  private String imageUrl;
  @Column(name = "image_name")
  private String imageName;

  @ManyToOne
  @JoinColumn(name = "property_id")
  private OwnerProperty property;

}
