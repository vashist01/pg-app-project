package com.kunj.repository;

import com.kunj.entity.Otp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Otp repository.
 */
@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

  /**
   * Find by otp and mobile number optional.
   *
   * @param otp          the otp
   * @param mobileNumber the mobile number
   * @return the optional
   */
  Optional<Otp> findByOtpAndMobileNumber(String otp, String mobileNumber);



}
