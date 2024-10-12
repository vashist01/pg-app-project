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

/**
 * The type Sub category.
 */
@Entity
@Table(name = "sub_category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "sub_category")
  private String subCategory;

  @ManyToOne
  @JoinColumn(name = "property_category_id")
  private PropertyCategory propertyCategory;
}
