package com.kunj.service;

import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.response.ProfileImageResponse;
import com.kunj.dto.response.PropertyResponseDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {

   void createUserProfile(UserProfileRequestDTO userProfileRequestDTO);

   void  updateUserProfile(UserProfileRequestDTO userProfileRequestDTO);

   ProfileImageResponse  uploadProfileImage(MultipartFile multipartFile);

   ProfileImageResponse getProfileImage();
}
