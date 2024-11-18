package com.kunj.controller;

import com.kunj.ResponseMessageConstant;
import com.kunj.dto.request.PropertyRequestDTO;
import com.kunj.dto.request.SearchPropertyRequestDTO;
import com.kunj.dto.response.ProfileImageResponse;
import com.kunj.dto.response.PropertyCategoryResponse;
import com.kunj.dto.response.PropertyImageResponse;
import com.kunj.dto.response.PropertyResponseDTO;
import com.kunj.dto.response.ResponseBO;
import com.kunj.enums.ApiEnum;
import com.kunj.exception.GenericController;
import com.kunj.service.PropertyService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * by http server handle 200 request if system is 8 core then it will handling 8 core 8 thread is
 * available 32 request at a time first 8 request take a 2 second in thread like 8*4 time so every 2
 * second 8 request will executed then after wait for released thread handling this thread execution
 * with virtual thread if spring.thread.virtual.enable=true not enable the 32 request take 8 sec. if
 * enable spring.thread.virtual.enable=true then 32 request take 2 sec. its more fast to
 */
@RestController
@RequestMapping(ApiEnum.API_VERSION)
@Valid
public class PropertyController extends GenericController {

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
  @RolesAllowed({"OWNER","ADMIN"})
  @PostMapping(ApiEnum.API_ADD_PROPERTY)
  public ResponseEntity<ResponseBO<List<PropertyResponseDTO>>> addProperty(
      @RequestBody PropertyRequestDTO propertyRequestDTO, HttpServletRequest httpServletRequest) {
    List<PropertyResponseDTO> propertyResponseDTO = propertyService.addProperty(
        propertyRequestDTO);
    return sendResponse(propertyResponseDTO, ResponseMessageConstant.SUCCESS, httpServletRequest);
  }

  /**
   * Get properties by owner response entity.
   *
   * @return the response entity
   */
  @GetMapping(ApiEnum.GET_PROPERTY)
  public ResponseEntity<ResponseBO<List<PropertyResponseDTO>>> getPropertiesByOwner(
      HttpServletRequest httpServletRequest) {
    List<PropertyResponseDTO> propertyResponseDTO = propertyService.getPropertiesByOwner();
    return sendResponse(propertyResponseDTO, ResponseMessageConstant.SUCCESS, httpServletRequest);
  }

  @GetMapping(ApiEnum.GET_ALL_PROPERTY_CATEGORY)
  public ResponseEntity<ResponseBO<Set<PropertyCategoryResponse>>> getAllPropertyCategory(
      HttpServletRequest httpServletRequest) {
    Set<PropertyCategoryResponse> propertyCategoryResponsesSet = propertyService.getAlLPropertyCategory();
    return sendResponse(propertyCategoryResponsesSet, ResponseMessageConstant.SUCCESS,
        httpServletRequest);
  }

  @PostMapping(ApiEnum.GET_PROPERTY_BY_LOCATION)
  public ResponseEntity<ResponseBO<Set<PropertyResponseDTO>>> getAllPropertyByLocation(@RequestBody
  SearchPropertyRequestDTO searchPropertyRequestDTO, HttpServletRequest httpServletRequest) {
    Set<PropertyResponseDTO> propertyCategoryResponsesSet = propertyService.getAllPropertyByLocation(
        searchPropertyRequestDTO);
    return sendResponse(propertyCategoryResponsesSet, ResponseMessageConstant.SUCCESS,
        httpServletRequest);
  }

  @GetMapping(ApiEnum.GET_PROPERTY_DETAIL_BY_ID)
  public ResponseEntity<ResponseBO<PropertyResponseDTO>> getPropertyDetailsById(
      @PathVariable String propertyId, HttpServletRequest httpServletRequest) {
    PropertyResponseDTO propertyCategoryResponsesSet = propertyService.getPropertyDetailsById(propertyId);
    return sendResponse(propertyCategoryResponsesSet, ResponseMessageConstant.SUCCESS,
        httpServletRequest);
  }

  @PostMapping(ApiEnum.UPLOAD_PROPERTY_IMAGE)
  public ResponseEntity<ResponseBO<PropertyImageResponse>> uploadPropertyImage(
      HttpServletRequest httpServletRequest,@Valid @NotNull @RequestParam("property_id") String propertyId,   @RequestParam("property_images") List<MultipartFile> multipartFileList) {
    PropertyImageResponse profileImageResponse = propertyService.uploadPropertyImage(multipartFileList,propertyId);
    return sendResponse(profileImageResponse, ResponseMessageConstant.SUCCESS, httpServletRequest);
  }
}
