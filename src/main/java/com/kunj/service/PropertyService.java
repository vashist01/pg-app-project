package com.kunj.service;

import com.kunj.dto.request.PropertyRequestDTO;
import com.kunj.dto.request.SearchPropertyRequestDTO;
import com.kunj.dto.response.PropertyCategoryResponse;
import com.kunj.dto.response.PropertyImageResponse;
import com.kunj.dto.response.PropertyResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface Property service.
 */
public interface PropertyService {

  /**
   * Add property response list.
   *
   * @param propertyRequestDTO the property request dto
   * @return the list
   */
  List<PropertyResponseDTO> addProperty(PropertyRequestDTO propertyRequestDTO);

  /**
   * Gets properties by owner.
   *
   * @return the properties by owner
   */
  List<PropertyResponseDTO> getPropertiesByOwner();

  List<PropertyResponseDTO> getAllNearestProperties();

  Set<PropertyCategoryResponse> getAlLPropertyCategory();

  Set<PropertyResponseDTO> getAllPropertyByLocation(SearchPropertyRequestDTO searchPropertyRequestDTO);

  PropertyResponseDTO getPropertyDetailsById(String propertyId);

  PropertyImageResponse uploadPropertyImage(List<MultipartFile> multipartFileList,
      @Valid @NotNull String imageId);
}
