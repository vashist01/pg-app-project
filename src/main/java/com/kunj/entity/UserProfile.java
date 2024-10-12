package com.kunj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfile implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String email;
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "date_of_birth")
  private String dateOfBirth;
  private String gender;
  @Column(name = "profile_picture",length = 5000)
  private String profilePicture;
  private String address;
  @Column(name = "created_at", updatable = false)
  private LocalDate createdAt;
  @Column(name = "updated_at")
  private LocalDate updatedAt;
  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;
}
