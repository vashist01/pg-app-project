package com.kunj.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Property category.
 */
@Table(name = "property_category")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name = "property_type")
  private String propertyType;

  @OneToMany(mappedBy = "propertyCategory", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<SubCategory> subCategorySet;
}
