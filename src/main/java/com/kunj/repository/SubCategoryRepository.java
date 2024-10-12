package com.kunj.repository;

import com.kunj.entity.SubCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Sub category repository.
 */
@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

  /**
   * Find by sub category in list.
   *
   * @param subCategoryList the sub category list
   * @return the list
   */
  List<SubCategory> findBySubCategoryIn(List<String> subCategoryList);
}
