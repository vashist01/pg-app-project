package com.kunj.controller;

import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.response.ProfileImageResponse;
import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.enums.ApiEnum;
import com.kunj.enums.ConstantEnums;
import com.kunj.service.UserProfileService;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiEnum.API_VERSION)
public class UserProfileController {
  private final UserProfileService userProfileService;

  public UserProfileController(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }


  @PostMapping(ApiEnum.CREATE_PROFILE)
  public ResponseEntity<Object> createUserProfile(@RequestBody UserProfileRequestDTO userProfileRequestDTO){
    userProfileService.createUserProfile(userProfileRequestDTO);
    return new ResponseEntity<>(ConstantEnums.SUCCESSFULLY_REGISTER.getValue(), HttpStatus.OK);
  }

  @PutMapping(ApiEnum.UPDATE_PROFILE)
  public ResponseEntity<Object> updateUserProfile(@RequestBody UserProfileRequestDTO userProfileRequestDTO){
      userProfileService.updateUserProfile(userProfileRequestDTO);
    return new ResponseEntity<>(ConstantEnums.SUCCESSFULLY_REGISTER.getValue(), HttpStatus.OK);
  }

  @PostMapping(ApiEnum.UPLOAD_PROFILE_IMAGE)
  public ResponseEntity<ProfileImageResponse> uploadProfileImage(@RequestParam("profile_image") MultipartFile multipartFile){
   ProfileImageResponse profileImageResponse =  userProfileService.uploadProfileImage(multipartFile);
    return new ResponseEntity<>(profileImageResponse, HttpStatus.OK);
  }
}
