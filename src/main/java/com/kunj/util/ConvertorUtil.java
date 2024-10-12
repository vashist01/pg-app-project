package com.kunj.util;

import com.kunj.dto.request.UserProfileRequestDTO;
import com.kunj.entity.User;
import com.kunj.entity.UserProfile;
import com.kunj.enums.DateTimeFormatEnum;
import com.kunj.enums.ValidationEnum;
import com.kunj.enums.ValidationMessageEnum;
import com.kunj.exception.custome.InValidMobileNumberException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * The type Convertor util.
 */
@Component
@Slf4j
public class ConvertorUtil {

  /**
   * Converts a hexadecimal string to a mobile number string.
   *
   * <p>This method converts a hexadecimal string to a decimal number, and then
   * to a string representation of a mobile number. It validates the resulting mobile number against
   * a predefined pattern. If the conversion or validation fails, it throws an
   * {@link InValidMobileNumberException}.
   *
   * @param hexaValue the hexadecimal string to be converted to a mobile number
   * @return the converted mobile number string if it is valid
   * @throws InValidMobileNumberException if the hexadecimal string cannot be converted         or
   *                                      if the resulting mobile number is invalid
   */
  public String convertCUIDNumberToMobile(String hexaValue) {
    try {
      // Convert hexadecimal string to a decimal number
      Long decimalValue = Long.parseLong(hexaValue, 16);
      String mobileNumber = String.valueOf(decimalValue);

      // Validate the converted mobile number
      if (ValidationEnum.MOBILE_NUMBER_PATTERN.validateMobileNumber(mobileNumber)) {
        return mobileNumber;
      }
      log.debug("Converted number is invalid as a mobile number: {}", mobileNumber);
      throw new InValidMobileNumberException(
          ValidationMessageEnum.INVALID_MOBILE_NUMBER.getMessage(),
          HttpStatus.BAD_REQUEST.value() + ""

      );
    } catch (NumberFormatException e) {
      log.debug("Failed to convert hexa value to number: {}", hexaValue, e);
      throw new InValidMobileNumberException(
          ValidationMessageEnum.INVALID_MOBILE_NUMBER.getMessage(),
          HttpStatus.BAD_REQUEST.value() + ""
      );
    }
  }

  /**
   * Converts a LocalDateTime object to a formatted LocalDateTime object.
   *
   * @return a LocalDateTime object formatted according to the pattern specified in
   * {@link ValidationEnum#DATE_TIME_FORMATE}
   * @throws DateTimeParseException the date time parse exception
   */
  public LocalDateTime convertLocalDateTimeToStanderdFormate() throws DateTimeParseException {
    LocalDateTime dateTime = LocalDateTime.now();
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(
        DateTimeFormatEnum.DATE_TIME_FORMAT.getValue());

    String formattedDateTime = dateTime.format(outputFormatter);
    return LocalDateTime.parse(formattedDateTime, outputFormatter);
  }

  public UserProfile convertUserProfileRequestDTOTOEntity(UserProfileRequestDTO userProfileRequestDTO,
      long userId,UserProfile userProfile) {
    if(Objects.isNull(userProfile)){
      userProfile = new UserProfile();
      userProfile.setCreatedAt(LocalDate.now());
    }else{
      userProfile.setUpdatedAt(LocalDate.now());
    }
    BeanUtils.copyProperties(userProfileRequestDTO,userProfile);
    userProfile.setUser(User.builder().id(userId).build());
  return  userProfile;
  }
}
