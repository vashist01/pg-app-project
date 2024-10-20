package com.kunj.controller;

import com.kunj.dto.request.UserDto;
import com.kunj.dto.response.UserResponse;
import com.kunj.enums.ApiEnum;
import com.kunj.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type User authentication controller.
 */
@RestController
@RequestMapping(ApiEnum.API_VERSION)
public class UserAuthenticationController {

  private final UserService userService;

  /**
   * Instantiates a new User authentication controller.
   *
   * @param userService the user service
   */
  public UserAuthenticationController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Login response entity.
   *
   * @param userDto the user dto
   * @return the response entity
   */
  @PostMapping(ApiEnum.LOGIN)
  public ResponseEntity<UserResponse> login(@RequestBody @Valid UserDto userDto) {
    return new ResponseEntity<>(userService.login(userDto), HttpStatus.OK);
  }
}
