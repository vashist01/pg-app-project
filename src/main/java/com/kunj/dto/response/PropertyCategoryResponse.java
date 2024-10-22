package com.kunj.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class PropertyCategoryResponse {

  private String categoryId;
  private String categoryName;
}
