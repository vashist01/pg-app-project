package com.kunj.controller;

import com.kunj.dto.request.PropertyRequestDTO;
import com.kunj.dto.request.SearchPropertyRequestDTO;
import com.kunj.dto.response.PropertyCategoryResponse;
import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.enums.ApiEnum;
import com.kunj.service.PropertyService;
import java.util.List;
import java.util.Set;
import jdk.jfr.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * by http server handle 200 request if system is 8 core then it will handling 8 core 8 thread is
 * available 32 request at a time first 8 request take a 2 second in thread like 8*4 time so every 2
 * second 8 request will executed then after wait for released thread handling this thread execution
 * with virtual thread if spring.thread.virtual.enable=true not enable the 32 request take 8 sec. if
 * enable spring.thread.virtual.enable=true then 32 request take 2 sec. its more fast to
 */
@RestController
@RequestMapping(ApiEnum.API_VERSION)
public class PropertyController {

  private final PropertyService propertyService;

  /**
   * Instantiates a new Property controller.
   *
   * @param propertyService the property service
   */
  public PropertyController(PropertyService propertyService) {
    this.propertyService = propertyService;
  }

  /**
   * Add property response entity.
   *
   * @param propertyRequestDTO the property request dto
   * @return the response entity
   */
  @PostMapping(ApiEnum.API_ADD_PROPERTY)
  public ResponseEntity<List<PropertyResponseDTO>> addProperty(
      @RequestBody PropertyRequestDTO propertyRequestDTO) {
    List<PropertyResponseDTO> propertyResponseDTO = propertyService.addProperty(
        propertyRequestDTO);
    return new ResponseEntity<>(propertyResponseDTO, HttpStatus.OK);
  }

  /**
   * Get properties by owner response entity.
   *
   * @return the response entity
   */
  @GetMapping(ApiEnum.GET_PROPERTY)
  public ResponseEntity<List<PropertyResponseDTO>> getPropertiesByOwner() {
    List<PropertyResponseDTO> propertyResponseDTO = propertyService.getPropertiesByOwner();
    return new ResponseEntity<>(propertyResponseDTO, HttpStatus.OK);
  }

  @GetMapping(ApiEnum.GET_ALL_PROPERTY_CATEGORY)
  public ResponseEntity<Set<PropertyCategoryResponse>> getAllPropertyCategory(){
    Set<PropertyCategoryResponse> propertyCategoryResponsesSet = propertyService.getAlLPropertyCategory();
    return new ResponseEntity<>(propertyCategoryResponsesSet, HttpStatus.OK);
  }

  @PostMapping(ApiEnum.GET_PROPERTY_BY_LOCATION)
  public ResponseEntity<Set<PropertyResponseDTO>> getAllPropertyByLocation(@RequestBody
  SearchPropertyRequestDTO searchPropertyRequestDTO){
    Set<PropertyResponseDTO> propertyCategoryResponsesSet = propertyService.getAllPropertyByLocation(searchPropertyRequestDTO);
    return new ResponseEntity<>(propertyCategoryResponsesSet, HttpStatus.OK);
  }
  @GetMapping(ApiEnum.GET_PROPERTY_DETAIL_BY_ID)
  public ResponseEntity<PropertyResponseDTO> getPropertyDetailsById(@PathVariable String propertyId){
    PropertyResponseDTO  propertyCategoryResponsesSet = propertyService.getPropertyDetailsById(propertyId);
    return new ResponseEntity<>(propertyCategoryResponsesSet, HttpStatus.OK);
  }

}
