package com.kunj.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.kunj.ResponseMessageConstant;
import com.kunj.dto.response.ProfileImageResponse;
import com.kunj.entity.ProfileImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class AwsMethodUtils {

  private static final String BUCKET_NAME =  "pg-profile-image-bucket";
  private static final String IMAGE_PATH_PREFIX =
      "profile-images/" + LocalDate.now() + ResponseMessageConstant.DELIMITER;

  private final AmazonS3 s3Client;

  public AwsMethodUtils(AmazonS3 s3Client) {
    this.s3Client = s3Client;
  }

  public Pair<String,String> uploadProfileImageToS3(MultipartFile multipartFile, Long id,
      Optional<ProfileImage> optionalProfileImage) {

      AtomicReference<String> originalFilename = new AtomicReference<>(
          multipartFile.getOriginalFilename());
    optionalProfileImage.ifPresent(profileImage -> originalFilename.set(profileImage.getFileName()));

    String fileName = IMAGE_PATH_PREFIX + id + ResponseMessageConstant.DELIMITER + (
        StringUtils.hasLength(originalFilename.get()) ? originalFilename.get().trim().replace(" ","") : originalFilename);
    String profileImageS3Url = "";

    try (InputStream inputStream = multipartFile.getInputStream()) {

      ObjectMetadata objectMetadata = getObjectMetaData(multipartFile, inputStream);

      s3Client.putObject(BUCKET_NAME, fileName, inputStream, objectMetadata);
      profileImageS3Url = s3Client.getUrl(BUCKET_NAME, fileName).toString();
    } catch (IOException ex) {
      log.error("Error at upload file :  ", ex);
    }
    return Pair.of(profileImageS3Url+"["+fileName+"]",originalFilename.get());
  }

  private ObjectMetadata getObjectMetaData(MultipartFile multipartFile, InputStream inputStream)
      throws IOException {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(inputStream.available());
    objectMetadata.setContentType(".png");
    objectMetadata.setContentLength(multipartFile.getSize());
    return objectMetadata;
  }

  public ProfileImageResponse getProfileImageFromS3(String profileImageToS3Url) {

    try (InputStream inputStream = readFileFromS3Object(profileImageToS3Url)) {

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

  private InputStream readFileFromS3Object(String profileImageToS3Url) {
    try {
      GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME,profileImageToS3Url);
      S3Object s3Object = s3Client.getObject(getObjectRequest);
      S3ObjectInputStream inputStream = s3Object.getObjectContent();
      return inputStream;
    } catch (Exception e) {
      log.error("No such key exists in S3: {}", profileImageToS3Url, e);
      return null; // Consider returning Optional<InputStream> for better handling
    }


  }

}
