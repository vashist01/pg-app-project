package com.kunj.service;

import com.kunj.entity.PropertyCategory;
import com.kunj.entity.SubCategory;
import com.kunj.repository.PropertyCategoryRepository;
import com.kunj.repository.SubCategoryRepository;
import com.kunj.util.components.FileUtility;
import jakarta.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * The type Property category service.
 */
@Service
@Slf4j
public class PropertyCategoryServiceImpl implements PropertyCategoryService {

  private final PropertyCategoryRepository propertyCategoryRepository;
  private final FileUtility fileUtility;
  private final SubCategoryRepository subCategoryRepository;

  /**
   * Instantiates a new Property category service.
   *
   * @param propertyCategoryRepository the property category repository
   * @param fileUtility                the file utility
   * @param subCategoryRepository      the sub category repository
   */
  public PropertyCategoryServiceImpl(PropertyCategoryRepository propertyCategoryRepository,
      FileUtility fileUtility, SubCategoryRepository subCategoryRepository) {
    this.propertyCategoryRepository = propertyCategoryRepository;
    this.fileUtility = fileUtility;
    this.subCategoryRepository = subCategoryRepository;
  }

  @Override
  @Transactional
  public void readDefaultPropertyCategory() throws FileNotFoundException {

    List<String> propertyCategoryList = addDefaultPropertyCategory();
    addDefaultSubPropertyCategory(propertyCategoryList);

  }


  /**
   * Adds default property categories to the database if they do not already exist.
   *
   * @return A list of all property categories, both existing and newly added.
   */
  private List<String> addDefaultPropertyCategory() {
    String fileLocation = "classpath:property_category.json";
    List<String> defaultCategory = fileUtility.readPropertyCategory(fileLocation);
    log.info("defalut catelgory list is : {} ", defaultCategory);

    List<String> existingCategories = propertyCategoryRepository.findByPropertyTypeIn(
        defaultCategory).stream().map(PropertyCategory::getPropertyType).toList();
    log.info("Existing property categories: {}", existingCategories);

    List<PropertyCategory> propertyCategoryList = new ArrayList<>();

    defaultCategory.forEach(category -> {

      if (!existingCategories.contains(category)) {

        log.info("Adding new property category: {}", category);
        PropertyCategory propertyCategory = new PropertyCategory();
        propertyCategory.setPropertyType(category);
        propertyCategoryList.add(propertyCategory);
      }
      log.info("Property category '{}' already exists, skipping.", category);
    });
    if (!CollectionUtils.isEmpty(propertyCategoryList)) {
      propertyCategoryRepository.saveAll(propertyCategoryList);
      log.info("Saving new property categories to the database: {}", propertyCategoryList);
    }
    log.info("No new property categories to save.");
    return defaultCategory;
  }


  /**
   * Adds default subcategories to existing property categories from a JSON file if they do not
   * already exist.
   *
   * @param propertyCategoryList List of property categories to check for and potentially add
   *                             subcategories to.
   */
  @Transactional
  private void addDefaultSubPropertyCategory(List<String> propertyCategoryList) {

    // Define the file path for reading default subcategories from JSON
    final String fileResourcePath = "classpath:sub-category.json";

    // Read the default categories from the JSON file
    Map<String, List<String>> defaultCategorySubCategoryMap = fileUtility.readSubCategoryFromJsonFile(
        fileResourcePath);
    log.info("Default subcategory list is : {}", defaultCategorySubCategoryMap);
    // Retrieve existing categories from the database
    List<PropertyCategory> existingCategories = propertyCategoryRepository.findByPropertyTypeIn(
        propertyCategoryList);

    existingCategories.forEach(propertyCategory -> {

      log.info("Processing subcategories for property type: {}", propertyCategory);
      List<String> subCategoryList = defaultCategorySubCategoryMap.get(
          propertyCategory.getPropertyType());
      saveDefaultSubCategoryIfNotExist(existingCategories, subCategoryList, propertyCategory);
    });
  }


  /**
   * Saves the default subcategories to the database if they do not already exist.
   *
   * @param existingCategories List of existing property categories.
   * @param subCategoryList    List of subcategory names to be checked and potentially saved.
   * @param propertyCategory
   */
  private void saveDefaultSubCategoryIfNotExist(List<PropertyCategory> existingCategories,
      List<String> subCategoryList, PropertyCategory propertyCategory) {

    List<String> existingSubCategories = subCategoryRepository.findBySubCategoryIn(
            subCategoryList)
        .stream().map(SubCategory::getSubCategory).toList().stream()
        .filter(sub -> !existingCategories.contains(sub)).toList();
    log.info("Existing subcategories: {}", existingSubCategories);

    if (CollectionUtils.isEmpty(existingSubCategories)) {
      if (!CollectionUtils.isEmpty(subCategoryList)) {
        subCategoryList.forEach(subs -> {

          log.info("Saving new subcategory: {}", subs);
          var subCategory = new SubCategory();
          subCategory.setPropertyCategory(propertyCategory);
          subCategory.setSubCategory(subs);
          subCategoryRepository.save(subCategory);
        });
      }
      log.warn("No subcategories to save as the subCategoryList is empty.");
    }
    log.info("No new subcategories to save, all provided subcategories already exist.");
  }
}