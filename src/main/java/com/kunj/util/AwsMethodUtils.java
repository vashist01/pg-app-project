package com.kunj.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kunj.dto.response.ProfileImageResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class AwsMethodUtils {

  private static final String BUCKET_NAME = "pg-app-bucket";
  private static final String IMAGE_PATH_PREFIX = "profile-images/" + LocalDate.now() + "/";

  private final AmazonS3 s3Client;

  public AwsMethodUtils(AmazonS3 s3Client) {
    this.s3Client = s3Client;
  }

  public String uploadProfileImageToS3(MultipartFile multipartFile, Long id) {
    String fileName = IMAGE_PATH_PREFIX + id;
    String profileImageS3Url = "";
    try (InputStream inputStream = multipartFile.getInputStream()) {
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(inputStream.available());
      objectMetadata.setContentType(".png");
      objectMetadata.setContentLength(multipartFile.getSize());
      s3Client.putObject(BUCKET_NAME, IMAGE_PATH_PREFIX, inputStream, objectMetadata);
      profileImageS3Url = s3Client.getUrl(BUCKET_NAME, fileName).toString();
    } catch (IOException ex) {
      log.error("Error at upload file : {} ",ex);
    }
    return profileImageS3Url;
  }

  public ProfileImageResponse getProfileImageFromS3(String profileImageToS3Url) {
    try (InputStream inputStream = s3Client.getObject(BUCKET_NAME, profileImageToS3Url)
        .getObjectContent()) {

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[4096];
      int bytesRead = 0;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        byteArrayOutputStream.write(buffer, 0, bytesRead);
      }
      byte[] imageBytes = byteArrayOutputStream.toByteArray();
      return ProfileImageResponse.builder()
          .profileImage(Base64.getEncoder().encodeToString(imageBytes)).build();
    } catch (IOException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return null;
  }
}
