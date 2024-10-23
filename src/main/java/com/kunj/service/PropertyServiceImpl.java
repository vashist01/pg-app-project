package com.kunj.service;

import com.kunj.dto.request.PropertyRequestDTO;
import com.kunj.dto.request.SearchPropertyRequestDTO;
import com.kunj.dto.response.PropertyCategoryResponse;
import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.entity.OwnerProperty;
import com.kunj.entity.PropertyCategory;
import com.kunj.repository.PropertyCategoryRepository;
import com.kunj.repository.PropertyRepository;
import com.kunj.util.components.UserProfileRequestScop;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The type Property service.
 */
@Service
@Slf4j
public class PropertyServiceImpl implements PropertyService {

  private final PropertyRepository propertyRepository;
  private final PropertyCategoryRepository propertyCategoryRepository;
  private final UserProfileRequestScop userProfile;

  /**
   * Instantiates a new Property service.
   *
   * @param propertyRepository         the property repository
   * @param propertyCategoryRepository the property category repository
   * @param userProfile                the user profile
   */
  public PropertyServiceImpl(PropertyRepository propertyRepository,
      PropertyCategoryRepository propertyCategoryRepository, UserProfileRequestScop userProfile) {
    this.propertyRepository = propertyRepository;
    this.propertyCategoryRepository = propertyCategoryRepository;
    this.userProfile = userProfile;
  }

  /**
   * @param propertyRequestDTO
   * @return
   */
  @Override
  public List<PropertyResponseDTO> addProperty(PropertyRequestDTO propertyRequestDTO) {

    log.info("add property details :{}  by User : {} ", propertyRequestDTO,
        userProfile.getMobileNumber());

    OwnerProperty ownerProperty = new OwnerProperty();
    BeanUtils.copyProperties(propertyRequestDTO, ownerProperty);
    ownerProperty.setUserId(1l);
    ownerProperty.setCategoryId(Long.parseLong(propertyRequestDTO.getCategoryId()));
    ownerProperty.setPropertyType(propertyRequestDTO.getPropertyCategory());
    ownerProperty.setTotalView(0);
    propertyRepository.save(ownerProperty);
    List<OwnerProperty> ownerPropertyList = propertyRepository.findByUserId(1l);
    return convertOwnerPropertyModelToResponseDTO(ownerPropertyList);
  }

  /**
   * @return
   */
  @Override
  public List<PropertyResponseDTO> getPropertiesByOwner() {
    List<OwnerProperty> ownerPropertyList = propertyRepository.findByUserId(userProfile.getId());
    return convertOwnerPropertyModelToResponseDTO(ownerPropertyList);
  }

  @Override
  public List<PropertyResponseDTO> getAllNearestProperties() {
    Pageable firstPageWithFiveElements = PageRequest.of(0, 5);
    List<OwnerProperty> ownerPropertyList =  propertyRepository.getNearest50Properties(firstPageWithFiveElements);
    return convertOwnerPropertyModelToResponseDTO(ownerPropertyList);
  }

  @Override
  public Set<PropertyCategoryResponse> getAlLPropertyCategory() {
    List<PropertyCategory> categoryResponses =  propertyCategoryRepository.findAll();
    return  categoryResponses.stream().map(propertyCategory -> {

      PropertyCategoryResponse propertyCategoryResponse = new PropertyCategoryResponse();
      propertyCategoryResponse.setCategoryName(propertyCategory.getPropertyType());
      propertyCategoryResponse.setCategoryId(propertyCategory.getId()+"");
      return propertyCategoryResponse;
    }).collect(Collectors.toSet());

  }

  @Override
  public Set<PropertyResponseDTO> getAllPropertyByLocation(SearchPropertyRequestDTO searchPropertyRequestDTO) {
    Pageable pageable = PageRequest.of(searchPropertyRequestDTO.getPage(), searchPropertyRequestDTO.getSize());

    // Fetch the paginated list of properties from the repository
    List<OwnerProperty> ownerPropertyList = propertyRepository.findAll(pageable).getContent();

    // Convert the list of OwnerProperty to PropertyResponseDTO
    List<PropertyResponseDTO> propertyResponseDTOS = convertOwnerPropertyModelToResponseDTO(ownerPropertyList);

    // Return as a Set to avoid duplicates
    return new HashSet<>(propertyResponseDTOS);
  }

  @Override
  public PropertyResponseDTO getPropertyDetailsById(String propertyId) {
    return propertyRepository.findById(Long.valueOf(propertyId)).map(property -> {
      PropertyResponseDTO propertyResponseDTO = new PropertyResponseDTO();
      BeanUtils.copyProperties(property,propertyResponseDTO);
      return propertyResponseDTO;
    }).orElseThrow(() -> new PropertyNotFoundException("Property Not found by  propertyId:"));

  }

  private List<PropertyResponseDTO> convertOwnerPropertyModelToResponseDTO(
      List<OwnerProperty> ownerPropertyList) {

    return ownerPropertyList.stream().map(property -> {
      PropertyResponseDTO propertyResponseDTO = new PropertyResponseDTO();
      BeanUtils.copyProperties(property,propertyResponseDTO);
      Optional<PropertyCategory> propertyCategory = propertyCategoryRepository.findById(
          Long.parseLong(property.getPropertyType()));
      propertyCategory.ifPresent(
          category -> propertyResponseDTO.setPropertyType(category.getPropertyType()));

      return propertyResponseDTO;
    }).toList();
  }


}
