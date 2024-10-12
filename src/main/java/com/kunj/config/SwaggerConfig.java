package com.kunj.config;//package com.kunj.config;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//  @Bean
//  public OpenAPI openAPI() {
//    return new OpenAPI().addSecurityItem(new SecurityRequirement().
//            addList("Bearer Authentication"))
//        .components(new Components().addSecuritySchemes
//            ("Bearer Authentication", createAPIKeyScheme()));
//  }
//  private SecurityScheme createAPIKeyScheme() {
//    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
//        .bearerFormat("JWT")
//        .scheme("bearer");
//  }
//}