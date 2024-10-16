package com.kunj.service;

import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.response.ProfileImageResponse;
import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.entity.ProfileImage;
import com.kunj.entity.User;
import com.kunj.entity.UserProfile;
import com.kunj.enums.RoleEnum;
import com.kunj.exception.custome.BadCredentialsException;
import com.kunj.repository.ProfileImageRepository;
import com.kunj.repository.UserProfileRepository;
import com.kunj.repository.UserRepository;
import com.kunj.util.AwsMethodUtils;
import com.kunj.util.ConvertorUtil;
import com.kunj.util.components.UserProfileRequestScop;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UserProfileServiceImpl implements UserProfileService {

  private final UserProfileRepository userProfileRepository;
  private final ConvertorUtil convertorUtil;
  private final UserProfileRequestScop userProfileRequestScop;
  private final UserRepository userRepository;
  private final  PropertyService propertyService;
  private  final ProfileImageRepository profileImageRepository;
  private  final  AwsMethodUtils awsMethodUtils;
  public UserProfileServiceImpl(UserProfileRepository userProfileRepository,
      ConvertorUtil convertorUtil, UserProfileRequestScop userProfileRequestScop,
       UserRepository userRepository, PropertyService propertyService,
      ProfileImageRepository profileImageRepository, AwsMethodUtils awsMethodUtils) {
    this.userProfileRepository = userProfileRepository;
    this.convertorUtil = convertorUtil;
    this.userProfileRequestScop = userProfileRequestScop;

    this.userRepository = userRepository;
    this.propertyService = propertyService;
    this.profileImageRepository = profileImageRepository;
    this.awsMethodUtils = awsMethodUtils;
  }

  @Override
  public void createUserProfile(UserProfileRequestDTO userProfileRequestDTO) {
    log.info("Creating user profile for mobile number: {}", userProfileRequestDTO.getMobileNumber());
    User user = registerUser(userProfileRequestDTO);

    log.info("User registered successfully with ID: {}", user.getId());
    com.kunj.entity.UserProfile userProfileEntity = convertorUtil.convertUserProfileRequestDTOTOEntity(
        userProfileRequestDTO, user.getId(), null);
    log.info("UserProfile entity converted from DTO for user ID: {}", user.getId());
    userProfileRepository.save(userProfileEntity);

  }

  private User registerUser(UserProfileRequestDTO userProfileRequestDTO) {
    Optional<User> optionalUser = userRepository.findByMobileNumber(
        userProfileRequestDTO.getMobileNumber());
    if (optionalUser.isEmpty()) {
      com.kunj.entity.User user = User.builder()
          .deviceSerialNumber(userProfileRequestDTO.getDeviceSerialNumber())
          .deviceToken(userProfileRequestDTO.getDeviceToken())
          .mobileNumber(userProfileRequestDTO.getMobileNumber())
          .role(userProfileRequestDTO.getRole())
          .createdAt(convertorUtil.convertLocalDateTimeToStanderdFormate())
          .createdBy(userProfileRequestDTO.getFirstName())
          .build();
      userRepository.save(user);
      return user;
    }
    throw new BadCredentialsException("User already register with this mobile Number : ", "20001");

  }

  @Override
  public void updateUserProfile(UserProfileRequestDTO userProfileRequestDTO) {

    Optional<UserProfile> userProfileOptional = userProfileRepository.findById(
        Long.valueOf(getUserIdAndRole().getRight()));

    if (userProfileOptional.isPresent()) {
      UserProfile userProfile = convertorUtil.convertUserProfileRequestDTOTOEntity(
          userProfileRequestDTO,
          userProfileRequestScop.getId(), userProfileOptional.get());
      userProfileRepository.save(userProfile);
    }

  }

  @Override
  public ProfileImageResponse   uploadProfileImage(MultipartFile multipartFile) {
      String profileImageToS3Url =  awsMethodUtils.uploadProfileImageToS3(multipartFile,userProfileRequestScop.getId());
      ProfileImage profileImage = ProfileImage.builder().profileImageS3Url(profileImageToS3Url).userId(
          userProfileRequestScop.getId()).build();
      profileImageRepository.save(profileImage);

    return awsMethodUtils.getProfileImageFromS3(profileImageToS3Url);
    }

  private List<PropertyResponseDTO> getPropertyListBasedOnRole() {
    Pair<Long, String> userProfilePairs = getUserIdAndRole();
    if (RoleEnum.OWNER.getRoleType().equalsIgnoreCase(userProfilePairs.getRight())) {
      return propertyService.getPropertiesByOwner();
    }
    return propertyService.getAllNearestProperties();
  }

  private Pair<Long, String> getUserIdAndRole() {
    return Pair.of(userProfileRequestScop.getId(), userProfileRequestScop.getRole());
  }
}
