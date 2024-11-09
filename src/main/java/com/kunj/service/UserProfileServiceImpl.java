package com.kunj.service;

import com.kunj.config.PropertyConfig;
import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.response.ProfileImageResponse;
import com.kunj.entity.ProfileImage;
import com.kunj.entity.User;
import com.kunj.entity.UserProfile;
import com.kunj.exception.custome.BadCredentialsException;
import com.kunj.exception.custome.InvalidException;
import com.kunj.repository.ProfileImageRepository;
import com.kunj.repository.UserProfileRepository;
import com.kunj.repository.UserRepository;
import com.kunj.util.AwsMethodUtils;
import com.kunj.util.ConvertorUtil;
import com.kunj.util.LoggerUtil;
import com.kunj.util.components.UserProfileRequestScop;
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
  private final PropertyService propertyService;
  private final ProfileImageRepository profileImageRepository;
  private final AwsMethodUtils awsMethodUtils;
  private final PropertyConfig propertyConfig;

  public UserProfileServiceImpl(UserProfileRepository userProfileRepository,
      ConvertorUtil convertorUtil, UserProfileRequestScop userProfileRequestScop,
      UserRepository userRepository, PropertyService propertyService,
      ProfileImageRepository profileImageRepository, AwsMethodUtils awsMethodUtils,
      PropertyConfig propertyConfig) {
    this.userProfileRepository = userProfileRepository;
    this.convertorUtil = convertorUtil;
    this.userProfileRequestScop = userProfileRequestScop;

    this.userRepository = userRepository;
    this.propertyService = propertyService;
    this.profileImageRepository = profileImageRepository;
    this.awsMethodUtils = awsMethodUtils;
    this.propertyConfig = propertyConfig;
  }

  @Override
  public void createUserProfile(UserProfileRequestDTO userProfileRequestDTO) {
    log.info("Creating user profile for mobile number: {}",
        userProfileRequestDTO.getMobileNumber());
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
  public ProfileImageResponse uploadProfileImage(MultipartFile multipartFile) {
    long userId = userProfileRequestScop.getId();
    Optional<ProfileImage> optionalProfileImage = profileImageRepository.findByUserId(userId);

    Pair<String, String> imageUrlAndFileName = awsMethodUtils.uploadProfileImageToS3(multipartFile,
        userId, optionalProfileImage, propertyConfig.getProfileImageBucketName());
    LoggerUtil.printLoggerWithINFO("Pair Data : {}", imageUrlAndFileName);
    saveUserProfileImage(imageUrlAndFileName, optionalProfileImage);

    return awsMethodUtils.getProfileImageFromS3(getImageURl(imageUrlAndFileName.getLeft(), 1),
        propertyConfig.getProfileImageBucketName());
  }

  @Override
  public ProfileImageResponse getProfileImage() {

    long userId = userProfileRequestScop.getId();
    Optional<ProfileImage> optionalProfileImage = profileImageRepository.findByUserId(userId);

    if (optionalProfileImage.isPresent()) {
      ProfileImage profileImage = optionalProfileImage.get();
      String profileImageUrl = awsMethodUtils.replaceImageUrlWithS3Url(
          profileImage.getProfileImageS3Url(), propertyConfig.getS3ProfileImageLocation());
      return awsMethodUtils.getProfileImageFromS3(profileImageUrl,
          propertyConfig.getProfileImageBucketName());
    }
    throw new InvalidException("User Not found ", "10008");
  }

  private void saveUserProfileImage(Pair<String, String> imageUrlAndFileName,
      Optional<ProfileImage> optionalProfileImage) {
    ProfileImage profileImage = optionalProfileImage.orElseGet(() ->
        ProfileImage.builder()
            .profileImageS3Url(getImageURl(imageUrlAndFileName.getLeft(), 0))
            .userId(userProfileRequestScop.getId())
            .fileName(imageUrlAndFileName.getRight())
            .build()
    );

    profileImageRepository.save(profileImage);
  }

  private String getImageURl(String imageUrls, int indexValue) {

    int indexOf = imageUrls.indexOf('[');
    if (indexValue == 1) {
      String s3ImageUrl = (indexOf != -1) ? imageUrls.substring(indexOf + indexValue) : imageUrls;
      return s3ImageUrl.replace("]", "");
    }
    return (indexOf != -1) ? imageUrls.substring(indexValue, indexOf) : imageUrls;
  }

  private Pair<Long, String> getUserIdAndRole() {
    return Pair.of(userProfileRequestScop.getId(), userProfileRequestScop.getRole());
  }
}
