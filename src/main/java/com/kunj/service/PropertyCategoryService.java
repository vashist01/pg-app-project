package com.kunj.service;

import java.io.FileNotFoundException;

/**
 * The interface Property category service.
 */
public interface PropertyCategoryService {

  /**
   * Read default property category.
   *
   * @throws FileNotFoundException the file not found exception
   */
  void readDefaultPropertyCategory() throws FileNotFoundException;
}