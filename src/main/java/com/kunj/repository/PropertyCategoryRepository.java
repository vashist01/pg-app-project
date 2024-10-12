package com.kunj.repository;

import com.kunj.entity.PropertyCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Property category repository.
 */
public interface PropertyCategoryRepository extends JpaRepository<PropertyCategory, Long> {

  /**
   * Find by property type in list.
   *
   * @param defaultCategory the default category
   * @return the list
   */
  List<PropertyCategory> findByPropertyTypeIn(List<String> defaultCategory);
}
