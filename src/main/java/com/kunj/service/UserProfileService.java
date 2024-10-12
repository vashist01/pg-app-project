package com.kunj.service;

import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.response.PropertyResponseDTO;
import java.util.List;

public interface UserProfileService {

  List<PropertyResponseDTO> createUserProfile(UserProfileRequestDTO userProfileRequestDTO);

  List<PropertyResponseDTO> updateUserProfile(UserProfileRequestDTO userProfileRequestDTO);
}
