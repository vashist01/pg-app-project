package com.kunj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type User.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pg_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "mobile_number")
  private String mobileNumber;

  @Column(name = "device_serial_number")
  private String deviceSerialNumber;

  @Column(name = "device_token")
  private String deviceToken;

  @Column(name = "role")
  private String role;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updateAt;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_by")
  private String updateBy;

  @OneToOne(mappedBy = "user")
  private UserAuthToken userAuthToken;
}
