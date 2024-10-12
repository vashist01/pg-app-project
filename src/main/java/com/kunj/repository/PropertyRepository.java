package com.kunj.repository;

import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.entity.OwnerProperty;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The interface Property repository.
 */
@Repository
public interface PropertyRepository extends JpaRepository<OwnerProperty, Long> {

  /**
   * Find by user id list.
   *
   * @param userId the user id
   * @return the list
   */
  List<OwnerProperty> findByUserId(Long userId);
  @Query("FROM OwnerProperty p")
  List<OwnerProperty> getNearest50Properties(Pageable firstPageWithTwoElements);
}
