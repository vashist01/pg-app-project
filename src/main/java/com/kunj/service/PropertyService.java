package com.kunj.service;

import com.kunj.dto.request.PropertyRequestDTO;
import com.kunj.dto.response.PropertyCategoryResponse;
import com.kunj.dto.response.PropertyResponseDTO;
import java.util.List;
import java.util.Set;

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
}
