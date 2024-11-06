package com.kunj.repository;

import com.kunj.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Find by mobile number optional.
   *
   * @param phoneNumber the phone number
   * @return the optional
   */
  Optional<User> findByMobileNumber(String phoneNumber);
}
