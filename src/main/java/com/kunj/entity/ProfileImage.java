package com.kunj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImage {
@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "profile_image_s3_url")
  private String profileImageS3Url;
  @Column(name = "user_id")
  private long userId;
}
