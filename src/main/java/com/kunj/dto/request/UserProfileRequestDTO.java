package com.kunj.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRequestDTO {

  @JsonProperty("user_name")
  private String username;
  private String email;
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  @JsonProperty("date_of_birth")
  private String dateOfBirth;
  private String gender;
  @JsonProperty("profile_image")
  private File profilePicture;
  private String address;
}
