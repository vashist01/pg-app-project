package com.kunj.controller;

import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.enums.ApiEnum;
import com.kunj.service.UserProfileService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEnum.API_VERSION)
public class UserProfileController {
  private final UserProfileService userProfileService;

  public UserProfileController(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }


  @PostMapping(ApiEnum.CREATE_PROFILE)
  public ResponseEntity<List<PropertyResponseDTO>> createUserProfile(@RequestBody UserProfileRequestDTO userProfileRequestDTO){
    List<PropertyResponseDTO> propertyResponseDTO = userProfileService.createUserProfile(userProfileRequestDTO);
    return new ResponseEntity<>(propertyResponseDTO, HttpStatus.OK);
  }

  @PutMapping(ApiEnum.UPDATE_PROFILE)
  public ResponseEntity<List<PropertyResponseDTO>> updateUserProfile(@RequestBody UserProfileRequestDTO userProfileRequestDTO){
    List<PropertyResponseDTO> propertyResponseDTO = userProfileService.updateUserProfile(userProfileRequestDTO);
    return new ResponseEntity<>(propertyResponseDTO, HttpStatus.OK);
  }
}
