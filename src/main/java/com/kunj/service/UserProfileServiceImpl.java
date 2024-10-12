package com.kunj.service;

import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.entity.UserProfile;
import com.kunj.enums.RoleEnum;
import com.kunj.repository.UserProfileRepository;
import com.kunj.util.ConvertorUtil;
import com.kunj.util.components.UserProfileRequestScop;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserProfileServiceImpl implements UserProfileService {
  private final UserProfileRepository userProfileRepository;
  private final ConvertorUtil convertorUtil;
  private final UserProfileRequestScop userProfileRequestScop;
  private final PropertyService propertyService;
  public UserProfileServiceImpl(UserProfileRepository userProfileRepository,
      ConvertorUtil convertorUtil, UserProfileRequestScop userProfileRequestScop,
      PropertyService propertyService) {
    this.userProfileRepository = userProfileRepository;
    this.convertorUtil = convertorUtil;
    this.userProfileRequestScop = userProfileRequestScop;
    this.propertyService = propertyService;
  }

  @Override
  public List<PropertyResponseDTO> createUserProfile(UserProfileRequestDTO userProfileRequestDTO) {

    com.kunj.entity.UserProfile userProfileEntity = convertorUtil.convertUserProfileRequestDTOTOEntity(userProfileRequestDTO,getUserIdAndRole().getLeft(),null);
    userProfileRepository.save(userProfileEntity);
    return getPropertyListBasedOnRole();
  }

  @Override
  public List<PropertyResponseDTO> updateUserProfile(UserProfileRequestDTO userProfileRequestDTO) {

    Optional<UserProfile> userProfileOptional = userProfileRepository.findById(
        Long.valueOf(getUserIdAndRole().getRight()));

    if(userProfileOptional.isPresent()){
      UserProfile userProfile = convertorUtil.convertUserProfileRequestDTOTOEntity(userProfileRequestDTO,
          userProfileRequestScop.getId(),userProfileOptional.get());
      userProfileRepository.save(userProfile);
    }
    return getPropertyListBasedOnRole();
  }
  private List<PropertyResponseDTO> getPropertyListBasedOnRole() {
    Pair<Long,String> userProfilePairs = getUserIdAndRole();
    if(RoleEnum.OWNER.getRoleType().equalsIgnoreCase(userProfilePairs.getRight())){
      return propertyService.getPropertiesByOwner();
    }
    return propertyService.getAllNearestProperties();
  }
  private Pair<Long, String> getUserIdAndRole() {
    return  Pair.of(userProfileRequestScop.getId(), userProfileRequestScop.getRole());
  }
}
