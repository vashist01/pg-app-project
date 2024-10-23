package com.kunj.dto.request;

import lombok.Data;

@Data
public class SearchPropertyRequestDTO {
  private String location;
  private int page;
  private int size;
}

