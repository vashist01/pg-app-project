package com.kunj.repository;

import com.kunj.entity.OwnerProperty;
import com.kunj.entity.PropertyImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PropertyImageRepository extends JpaRepository<PropertyImage,Long> {
  List<PropertyImage> findByOwnerProperty(OwnerProperty ownerProperty);
}
