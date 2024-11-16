package com.kunj.service;

import com.kunj.ResponseMessageConstant;
import com.kunj.config.PropertyConfig;
import com.kunj.dto.projection.PropertyImageProjection;
import com.kunj.dto.request.PropertyRequestDTO;
import com.kunj.dto.request.SearchPropertyRequestDTO;
import com.kunj.dto.response.ImageResponseDTO;
import com.kunj.dto.response.PropertyCategoryResponse;
import com.kunj.dto.response.PropertyImageResponse;
import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.entity.OwnerProperty;
import com.kunj.entity.PropertyCategory;
import com.kunj.entity.PropertyImage;
import com.kunj.exception.custome.FileUploadException;
import com.kunj.exception.custome.ValidationException;
import com.kunj.repository.PropertyCategoryRepository;
import com.kunj.repository.PropertyImageRepository;
import com.kunj.repository.PropertyRepository;
import com.kunj.util.AwsMethodUtils;
import com.kunj.util.LoggerUtil;
import com.kunj.util.components.UserProfileRequestScop;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.PropertyNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Property service.
 */
@Service
public class PropertyServiceImpl implements PropertyService {

  private final PropertyRepository propertyRepository;
  private final PropertyCategoryRepository propertyCategoryRepository;
  private final UserProfileRequestScop userProfile;
  private final AwsMethodUtils awsMethodUtils;
  private final PropertyConfig propertyConfig;
  private final PropertyImageRepository propertyImageRepository;

  /**
   * Instantiates a new Property service.
   *
   * @param propertyRepository         the property repository
   * @param propertyCategoryRepository the property category repository
   * @param userProfile                the user profile
   */
  public PropertyServiceImpl(PropertyRepository propertyRepository,
      PropertyCategoryRepository propertyCategoryRepository, UserProfileRequestScop userProfile,
      AwsMethodUtils awsMethodUtils, PropertyConfig propertyConfig,
      PropertyImageRepository propertyImageRepository) {
    this.propertyRepository = propertyRepository;
    this.propertyCategoryRepository = propertyCategoryRepository;
    this.userProfile = userProfile;
    this.awsMethodUtils = awsMethodUtils;
    this.propertyConfig = propertyConfig;
    this.propertyImageRepository = propertyImageRepository;
  }


  @Override
  public List<PropertyResponseDTO> addProperty(PropertyRequestDTO propertyRequestDTO) {
    long userId = userProfile.getId();

    LoggerUtil.printLoggerWithINFO("add property details :{}  by User : {} ", propertyRequestDTO,
        userProfile.getMobileNumber());
    OwnerProperty ownerProperty = new OwnerProperty();
    BeanUtils.copyProperties(propertyRequestDTO, ownerProperty);
    ownerProperty.setUserId(userId);
    ownerProperty.setCategoryId(Long.parseLong(propertyRequestDTO.getCategoryId()));
    ownerProperty.setPropertyType(propertyRequestDTO.getPropertyCategory());
    ownerProperty.setTotalView(0);
    propertyRepository.save(ownerProperty);
    Pageable pageable = PageRequest.of(0, 5);
    List<OwnerProperty> ownerPropertyList = propertyRepository.findByUserId(userId, pageable);
    LoggerUtil.printLoggerWithINFO(" property details :{}  by User : {} ", ownerPropertyList);
    return convertOwnerPropertyModelToResponseDTO(ownerPropertyList);
  }


  @Override
  public List<PropertyResponseDTO> getPropertiesByOwner() {
    Pageable pageable = PageRequest.of(0, 5);
    List<OwnerProperty> ownerPropertyList = propertyRepository.findByUserId(userProfile.getId(),
        pageable);
    return convertOwnerPropertyModelToResponseDTO(ownerPropertyList);
  }

  @Override
  public List<PropertyResponseDTO> getAllNearestProperties() {
    Pageable firstPageWithFiveElements = PageRequest.of(0, 5);
    List<OwnerProperty> ownerPropertyList = propertyRepository.getNearest50Properties(
        firstPageWithFiveElements);
    return convertOwnerPropertyModelToResponseDTO(ownerPropertyList);
  }

  @Override
  public Set<PropertyCategoryResponse> getAlLPropertyCategory() {
    List<PropertyCategory> categoryResponses = propertyCategoryRepository.findAll();
    return categoryResponses.stream().map(propertyCategory -> {

      PropertyCategoryResponse propertyCategoryResponse = new PropertyCategoryResponse();
      propertyCategoryResponse.setCategoryName(propertyCategory.getPropertyType());
      propertyCategoryResponse.setCategoryId(propertyCategory.getId() + "");
      return propertyCategoryResponse;
    }).collect(Collectors.toSet());

  }

  @Override
  public Set<PropertyResponseDTO> getAllPropertyByLocation(
      SearchPropertyRequestDTO searchPropertyRequestDTO) {
    Pageable pageable = PageRequest.of(searchPropertyRequestDTO.getPage(),
        searchPropertyRequestDTO.getSize());
    List<OwnerProperty> ownerPropertyList = propertyRepository.findAll(pageable).getContent();
    List<PropertyResponseDTO> propertyResponseDTOS = convertOwnerPropertyModelToResponseDTO(
        ownerPropertyList);
    return new HashSet<>(propertyResponseDTOS);
  }

  @Override
  public PropertyResponseDTO getPropertyDetailsById(String propertyId) {
    return propertyRepository.findById(Long.valueOf(propertyId)).map(property -> {
      PropertyResponseDTO propertyResponseDTO = new PropertyResponseDTO();
      BeanUtils.copyProperties(property, propertyResponseDTO);

      List<String> propertyImageList = getPropertyImageFromS3ByPropertyId(
          property.getPropertyImages());
      propertyResponseDTO.setPropertyImage(propertyImageList);
      return propertyResponseDTO;
    }).orElseThrow(() -> new PropertyNotFoundException("Property Not found by  propertyId:"));

  }

  @Override
  public PropertyImageResponse uploadPropertyImage(List<MultipartFile> multipartFileList,
      @Valid @NotNull String propertyId) {

    if (CollectionUtils.isEmpty(multipartFileList)) {
      throw new FileUploadException(ResponseMessageConstant.FILE_UPLOAD_ERROR_MESSAGE,
          ResponseMessageConstant.ERROR_CODE);
    }
    return uploadPropertyImagesInS3(multipartFileList, propertyId);
  }

  private PropertyImageResponse uploadPropertyImagesInS3(List<MultipartFile> multipartFileList,
      @Valid @NotNull String propertyId) {
    OwnerProperty ownerProperty = new OwnerProperty();
    ownerProperty.setId(Long.parseLong(propertyId));
    return uploadPropertyImageUniqueFileInS3(multipartFileList, ownerProperty);
  }

  private PropertyImageResponse uploadPropertyImageUniqueFileInS3(
      List<MultipartFile> multipartFileList,
      OwnerProperty ownerProperty) {

    String fileName = ResponseMessageConstant.IMAGE_PATH_PREFIX + userProfile.getId()
        + ResponseMessageConstant.DELIMITER + ownerProperty.getId()
        + ResponseMessageConstant.DELIMITER;

    List<PropertyImage> propertyImageList = propertyImageRepository.findByOwnerProperty(
        ownerProperty);
    if (propertyImageList.size() == Integer.parseInt(propertyConfig.getUploadPropertyFileLimit())) {
      throw new ValidationException(ResponseMessageConstant.FILE_UPLOAD_LIMIT_EXCEED,
          ResponseMessageConstant.ERROR_CODE);
    }
    if (!CollectionUtils.isEmpty(multipartFileList)) {
      List<PropertyImage> profileImagesList = multipartFileList.stream()
          .map(multipartFile -> uploadProfileImageInS3IfNotExist(multipartFile, fileName,
              ownerProperty))
          .toList();
      List<PropertyImage> profileImageList = propertyImageRepository.saveAll(profileImagesList);
      return getPropertyImageResponse(ownerProperty);
    }
    throw new FileUploadException(ResponseMessageConstant.FILE_UPLOAD_ERROR_MESSAGE,
        ResponseMessageConstant.ERROR_CODE);
  }

  private PropertyImageResponse getPropertyImageIfAlreadyExist(
      List<PropertyImageProjection> propertyImagesList) {
    PropertyImageResponse propertyImageResponse = new PropertyImageResponse();
    List<ImageResponseDTO> imageResponseDTOS = new ArrayList<>();
    for (PropertyImageProjection propertyImageProjection : propertyImagesList) {

      String url = convertPropertyImageToBase64String(propertyImageProjection.getImageUrl());
      ImageResponseDTO imageResponseDTO = new ImageResponseDTO();
      imageResponseDTO.setPropertyImage(url);
      imageResponseDTOS.add(imageResponseDTO);

    }
    propertyImageResponse.setPropertyImageList(imageResponseDTOS);
    return propertyImageResponse;
  }

  private PropertyImageResponse getPropertyImageResponse(OwnerProperty ownerProperty) {
    List<PropertyImage> propertyImageList = propertyImageRepository.findByOwnerProperty(ownerProperty);
    if (CollectionUtils.isEmpty(propertyImageList)) {
      return null;
    }
    List<ImageResponseDTO> propertyImageBase64String = propertyImageList.stream()
        .map(this::readPropertyImageFromS3).toList();
    PropertyImageResponse propertyImageResponse = new PropertyImageResponse();

    propertyImageResponse.setPropertyImageList(propertyImageBase64String);
    return propertyImageResponse;
  }

  private ImageResponseDTO readPropertyImageFromS3(PropertyImage propertyImage) {
    String imageUrl = awsMethodUtils.replaceImageUrlWithS3Url(
        propertyImage.getPropertyImageUrl(), propertyConfig.getS3ProfileImageLocation());
    return ImageResponseDTO.builder().propertyImage(
            awsMethodUtils.readFileFromS3(imageUrl, propertyConfig.getProfileImageBucketName()))
        .build();
  }

  private PropertyImage uploadProfileImageInS3IfNotExist(MultipartFile multipartFile,
      String fileName,
      OwnerProperty ownerProperty) {
    PropertyImage propertyImage = new PropertyImage();
    if(Objects.nonNull(multipartFile) && StringUtils.hasText(multipartFile.getOriginalFilename())){
      String originalFileName = multipartFile.getOriginalFilename().replace(" ", "");
      propertyImage.setImageName(originalFileName);
      String multipartFileName = fileName + originalFileName;
      String s3Url = awsMethodUtils.uploadImageToS3Bucket(
          propertyConfig.getProfileImageBucketName(), multipartFileName.trim(), multipartFile);
      propertyImage.setPropertyImageUrl(s3Url);
      propertyImage.setOwnerProperty(ownerProperty);
      propertyImage.setUserId(userProfile.getId());
    }
    return propertyImage;
  }

  private List<String> getPropertyImageFromS3ByPropertyId(List<PropertyImage> propertyImages) {
    if (propertyImages.isEmpty()) {
      return Collections.emptyList();
    }

    return propertyImages.stream().map(
            propertyImage -> convertPropertyImageToBase64String(propertyImage.getPropertyImageUrl()))
        .toList();
  }

  private String convertPropertyImageToBase64String(String propertyImageUrl) {
    String imageUrl = awsMethodUtils.replaceImageUrlWithS3Url(propertyImageUrl,
        propertyConfig.getS3ProfileImageLocation());
    return awsMethodUtils.readFileFromS3(imageUrl,
        propertyConfig.getProfileImageBucketName());
  }

  private List<PropertyResponseDTO> convertOwnerPropertyModelToResponseDTO(
      List<OwnerProperty> ownerPropertyList) {

    return ownerPropertyList.stream().map(property -> {
      PropertyResponseDTO propertyResponseDTO = new PropertyResponseDTO();
      BeanUtils.copyProperties(property, propertyResponseDTO);
      Optional<PropertyCategory> propertyCategory = propertyCategoryRepository.findById(
          Long.parseLong(property.getPropertyType()));
      propertyCategory.ifPresent(
          category -> propertyResponseDTO.setPropertyType(category.getPropertyType()));

      return propertyResponseDTO;
    }).toList();
  }
}
