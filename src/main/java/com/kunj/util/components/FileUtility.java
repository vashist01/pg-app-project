package com.kunj.util.components;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * The type File utility.
 */
@Component
@Slf4j
public class FileUtility {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ResourceLoader resourceLoader;

  /**
   * Instantiates a new File utility.
   *
   * @param resourceLoader the resource loader
   */
  public FileUtility(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  /**
   * Read property category list.
   *
   * @param fileLocation the file location
   * @return the list
   */
  public List<String> readPropertyCategory(String fileLocation) {
    log.info("read Property category ........");
    Resource classPathResource = resourceLoader.getResource(fileLocation);

    try (InputStream inputStream = classPathResource.getInputStream()) {
      return objectMapper.readValue(inputStream, new TypeReference<List<String>>() {
      });
    } catch (IOException ioException) {
      log.warn("Failed to read JSON file from classpath resource: {}", ioException.getMessage());
      return Collections.emptyList();
    }
  }

  /**
   * Read sub category from json file map.
   *
   * @param fileResourcePath the file resource path
   * @return the map
   */
  public Map<String, List<String>> readSubCategoryFromJsonFile(String fileResourcePath) {
    log.info("Default subcategory list is : {}");
    Resource classPathResource = resourceLoader.getResource(fileResourcePath);
    try (InputStream inputStream = classPathResource.getInputStream()) {

      Map<String, List<String>> subCategoryData = objectMapper.readValue(inputStream, Map.class);
      log.info("imported subcategory file data is :: {}", subCategoryData);
      return subCategoryData;
    } catch (IOException ioException) {
      log.warn("Failed to read JSON file from classpath resource: {}", ioException.getMessage());
      return Collections.emptyMap();
    }
  }
}
