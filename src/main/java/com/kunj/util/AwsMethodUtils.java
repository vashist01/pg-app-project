package com.kunj.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.kunj.ResponseMessageConstant;
import com.kunj.dto.response.ProfileImageResponse;
import com.kunj.entity.ProfileImage;
import com.kunj.exception.custome.FileUploadException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class AwsMethodUtils {


  private static final String IMAGE_PATH_PREFIX =
      "profile-images" + ResponseMessageConstant.DELIMITER;

  private final AmazonS3 s3Client;

  public Pair<String, String> uploadProfileImageToS3(MultipartFile multipartFile, Long id,
      Optional<ProfileImage> optionalProfileImage, String profileImageBucketName) {

    AtomicReference<String> originalFilename = new AtomicReference<>(
        multipartFile.getOriginalFilename());
    optionalProfileImage.ifPresent(
        profileImage -> originalFilename.set(profileImage.getFileName()));

    String fileName = IMAGE_PATH_PREFIX + id + ResponseMessageConstant.DELIMITER + (
        StringUtils.hasLength(originalFilename.get()) ? originalFilename.get().trim()
            .replace(" ", "") : originalFilename);

    return Pair.of(
        uploadImageToS3Bucket(profileImageBucketName, fileName, multipartFile) + "[" + fileName
            + "]", originalFilename.get());
  }

  public AwsMethodUtils(AmazonS3 s3Client) {
    this.s3Client = s3Client;
  }

  public String uploadImageToS3Bucket(String profileImageBucketName, String fileName,
      MultipartFile multipartFile) {

    try (InputStream inputStream = multipartFile.getInputStream()) {
      ObjectMetadata objectMetadata = getObjectMetaData(multipartFile, inputStream);
      s3Client.putObject(profileImageBucketName, fileName, inputStream, objectMetadata);
      return s3Client.getUrl(profileImageBucketName, fileName).toString();

    } catch (Exception ex) {
      LoggerUtil.printLoggerWithERROR(ResponseMessageConstant.FILE_UPLOAD_ERROR_MESSAGE, ex,
          fileName);
    }
    throw new FileUploadException(ResponseMessageConstant.FILE_UPLOAD_ERROR_MESSAGE,
        ResponseMessageConstant.ERROR_CODE);
  }

  private ObjectMetadata getObjectMetaData(MultipartFile multipartFile, InputStream inputStream)
      throws IOException {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(inputStream.available());
    objectMetadata.setContentType(".png");
    objectMetadata.setContentLength(multipartFile.getSize());
    return objectMetadata;
  }

  public ProfileImageResponse getProfileImageFromS3(String profileImageToS3Url,
      String profileImageBucketName) {
    String base64String = readFileFromS3(profileImageToS3Url, profileImageBucketName);
    return ProfileImageResponse.builder()
        .profileImage(base64String).build();
  }


  public String readFileFromS3(String s3ImageUrl, String bucketName) {
    LoggerUtil.printLoggerWithINFO("S3 image Url : {} ", s3ImageUrl);
    try (InputStream inputStream = readFileFromS3Object(s3ImageUrl, bucketName)) {

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[4096];
      int bytesRead = 0;

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        byteArrayOutputStream.write(buffer, 0, bytesRead);
      }
      byte[] imageBytes = byteArrayOutputStream.toByteArray();
      return Base64.getEncoder().encodeToString(imageBytes);

    } catch (IOException e) {
      LoggerUtil.printLoggerWithERROR(
          ResponseMessageConstant.FILE_TO_READ_FILE_FROM_S3_ERROR_MESSAGE, e, s3ImageUrl);
    }
    return null;
  }

  private InputStream readFileFromS3Object(String s3ImageUrl, String bucketName) {
    try {
      GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, s3ImageUrl);
      S3Object s3Object = s3Client.getObject(getObjectRequest);
      return s3Object.getObjectContent();
    } catch (Exception e) {
      LoggerUtil.printLoggerWithERROR(
          ResponseMessageConstant.FILE_TO_READ_FILE_FROM_S3_ERROR_MESSAGE, e, s3ImageUrl);
    }
    throw new FileUploadException(ResponseMessageConstant.FILE_TO_READ_FILE_FROM_S3_ERROR_MESSAGE,
        ResponseMessageConstant.ERROR_CODE);
  }

  public String replaceImageUrlWithS3Url(String s3ProfileImageUrl, String s3ProfileImageLocation) {

    if (s3ProfileImageUrl != null && s3ProfileImageUrl.contains(s3ProfileImageLocation)) {
      return s3ProfileImageUrl.replace(s3ProfileImageLocation, "");
    }
    return s3ProfileImageUrl;
  }

}
