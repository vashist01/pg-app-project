package com.kunj.util;

import com.kunj.dto.request.PropertyRequestDTO;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerUtil {

  private LoggerUtil(){
    log.info("private constructor for initiate once s");
  }

  public static void printLoggerWithINFO(String loggerMessage, Object object) {
    log.info(loggerMessage,object);
  }

  public static void printLoggerWithERROR(String loggerMessage, Exception exception, String object) {
    log.error(loggerMessage,exception,object);
  }

  public static void printLoggerWithINFO(String loggerMessage, Object propertyRequestDTO, Object mobileNumber) {
    log.info(loggerMessage,propertyRequestDTO,mobileNumber);
  }
}
