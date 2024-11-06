package com.kunj.repository;

import com.kunj.entity.ProfileImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

  Optional<ProfileImage> findByUserId(long userId);
}
