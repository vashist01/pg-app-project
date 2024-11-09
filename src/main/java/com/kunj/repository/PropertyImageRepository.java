package com.kunj.repository;

import com.kunj.dto.projection.PropertyImageProjection;
import com.kunj.entity.OwnerProperty;
import com.kunj.entity.PropertyImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PropertyImageRepository extends JpaRepository<PropertyImage,Long> {


  @Query("SELECT pi.imageName AS imageName, pi.propertyImageUrl AS imageUrl FROM PropertyImage pi WHERE pi.ownerProperty = :ownerProperty AND pi.userId = :userId")
  List<PropertyImageProjection> findByOwnerPropertyAndUserId(@Param("ownerProperty") OwnerProperty ownerProperty, @Param("userId") long userId);
}
