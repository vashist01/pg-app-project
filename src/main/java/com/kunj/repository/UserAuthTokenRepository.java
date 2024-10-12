package com.kunj.repository;

import com.kunj.entity.UserAuthToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The interface User auth token repository.
 */
@Repository
public interface UserAuthTokenRepository extends JpaRepository<UserAuthToken, Long> {

  /**
   * Find by user id optional.
   *
   * @param userId the user id
   * @return the optional
   */
  @Query("From UserAuthToken uat where uat.user.id=:userId")
  Optional<UserAuthToken> findByUserId(long userId);
}
