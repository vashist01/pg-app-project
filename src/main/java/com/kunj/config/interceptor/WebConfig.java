package com.kunj.config.interceptor;

import com.kunj.enums.ApiEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Web config.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private static final Logger log = LoggerFactory.getLogger(WebConfig.class);
  private static final String[] AUTH_WHITELIST = {
      "/v2/api-docs",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui.html",
      "/webjars/**",
      "/v3/api-docs/**",
      "/swagger-ui/**"
  };
  private final CommonAuthValidatorInterceptor commonAuthValidatorInterceptor;

  /**
   * Instantiates a new Web config.
   *
   * @param commonAuthValidatorInterceptor the common auth validator interceptor
   */
  public WebConfig(CommonAuthValidatorInterceptor commonAuthValidatorInterceptor) {
    this.commonAuthValidatorInterceptor = commonAuthValidatorInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(commonAuthValidatorInterceptor).excludePathPatterns(getExcludePath());
  }

  private List<String> getExcludePath() {
    log.info("exclude path execution ");
    final String API_APTH = ApiEnum.API_VERSION;
    List<String> excludeApiPath = new ArrayList<>();
    excludeApiPath.add(API_APTH + ApiEnum.USER_REGISTER);
    excludeApiPath.add(API_APTH + ApiEnum.CREATE_PROFILE);
    excludeApiPath.addAll(Arrays.stream(AUTH_WHITELIST).toList());
    return excludeApiPath;
  }
}
