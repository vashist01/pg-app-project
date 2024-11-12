package com.kunj.controller;

import com.kunj.ResponseMessageConstant;
import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.response.ProfileImageResponse;
import com.kunj.dto.response.ResponseBO;
import com.kunj.enums.ApiEnum;
import com.kunj.enums.ConstantEnums;
import com.kunj.exception.GenericController;
import com.kunj.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiEnum.API_VERSION)
public class UserProfileController extends GenericController {

  private final UserProfileService userProfileService;

  public UserProfileController(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }



  @PutMapping(ApiEnum.UPDATE_PROFILE)
  public ResponseEntity<Object> updateUserProfile(
      @RequestBody UserProfileRequestDTO userProfileRequestDTO) {
    userProfileService.updateUserProfile(userProfileRequestDTO);
    return new ResponseEntity<>(ConstantEnums.SUCCESSFULLY_REGISTER.getValue(), HttpStatus.OK);
  }

  @PostMapping(ApiEnum.UPLOAD_PROFILE_IMAGE)
  public ResponseEntity<ResponseBO<ProfileImageResponse>> uploadProfileImage(
      @RequestParam("profile_image") MultipartFile multipartFile,
      HttpServletRequest httpServletRequest) {
    ProfileImageResponse profileImageResponse = userProfileService.uploadProfileImage(
        multipartFile);
    return sendResponse(profileImageResponse, ResponseMessageConstant.SUCCESS, httpServletRequest);

  }


  @GetMapping(ApiEnum.PROFILE_IMAGE)
  public ResponseEntity<ResponseBO<ProfileImageResponse>> getProfileImage(
      HttpServletRequest httpServletRequest) {
    ProfileImageResponse profileImageResponse = userProfileService.getProfileImage();
    return sendResponse(profileImageResponse, ResponseMessageConstant.SUCCESS, httpServletRequest);
  }
}
