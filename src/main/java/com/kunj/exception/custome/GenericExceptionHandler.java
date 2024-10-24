//package com.kunj.exception.custome;
//
//
//import com.kunj.dto.response.ErrorBo;
//import com.kunj.dto.response.ResponseBO;
//import com.kunj.exception.GenericController;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.ConstraintViolationException;
//import jakarta.validation.UnexpectedTypeException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSourceResolvable;
//import org.springframework.core.task.TaskRejectedException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.stereotype.Component;
//import org.springframework.web.HttpMediaTypeNotSupportedException;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.method.annotation.HandlerMethodValidationException;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//
//@Component
//@Slf4j
//public class GenericExceptionHandler {
//  protected final GenericController genericController;
//
//  public GenericExceptionHandler(GenericController genericController) {
//    this.genericController = genericController;
//  }
//
//
//  @ExceptionHandler(BaseException.class)
//  public ResponseEntity<ResponseBO<Object>> handleBaseException(BaseException exception,
//      HttpServletRequest request) {
//    return genericController.sendFailure(null, getError(exception), 1,
//        request);
//  }
//
//  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//  public ResponseEntity<ResponseBO<Object>> handleHttpRequestMethodNotSupportedException(
//      HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
//    ErrorBo error = ErrorBo.builder().errorCode("10001")
//        .errorMessage(exception.getMessage()).build();
//
//    return genericController.sendFailure(null, error,
//        HttpStatus.METHOD_NOT_ALLOWED.value(), request);
//  }
//
//  @ExceptionHandler(ConstraintViolationException.class)
//  public ResponseEntity<ResponseBO<Object>> handleConstraintViolationException(
//      ConstraintViolationException exception,
//      HttpServletRequest request) {
//
//    return genericController.sendFailure(null, getError(exception), HttpStatus.BAD_REQUEST.value(), request);
//
//  }
//
//  @ExceptionHandler(MethodArgumentNotValidException.class)
//  public ResponseEntity<ResponseBO<Object>> handleMethodArgumentNotValidException(
//      MethodArgumentNotValidException exception, HttpServletRequest request) {
//
//    ErrorBo error = ErrorBo.builder().errorCode("10001")
//        .errorMessage(exception.getMessage()).build();
//
//    return genericController.sendFailure(null, error, HttpStatus.BAD_REQUEST.value(), request);
//  }
//
//  @ExceptionHandler(HandlerMethodValidationException.class)
//  public ResponseEntity<ResponseBO<Object>> handleHandlerMethodValidationException(
//      HandlerMethodValidationException exception, HttpServletRequest request) {
//    return genericController.sendFailure(null, getError(exception), HttpStatus.BAD_REQUEST.value(), request);
//  }
//
//  @ExceptionHandler(HttpMessageNotReadableException.class)
//  public ResponseEntity<ResponseBO<Object>> handleHttpMessageNotReadableException(
//      HttpMessageNotReadableException exception, HttpServletRequest request) {
//
//    return genericController.sendFailure(null, getError(exception), HttpStatus.BAD_REQUEST.value(), request);
//  }
//
//  @ExceptionHandler(TaskRejectedException.class)
//  public ResponseEntity<ResponseBO<Object>> handleTaskRejectedException(
//      TaskRejectedException exception,
//      HttpServletRequest request) {
//
//    return genericController.sendFailure(null,getError(exception),
//        HttpStatus.INTERNAL_SERVER_ERROR.value(), request);
//  }
//
//  @ExceptionHandler(UnexpectedTypeException.class)
//  public ResponseEntity<ResponseBO<Object>> handleUnexpectedTypeException(
//      UnexpectedTypeException exception,
//      HttpServletRequest request) {
//
//    return genericController.sendFailure(null, getError(exception),
//        HttpStatus.BAD_REQUEST.value(),
//        request);
//  }
//
//  @ExceptionHandler(RuntimeException.class)
//  public ResponseEntity<ResponseBO<Object>> handleRuntimeException(RuntimeException exception,
//      HttpServletRequest request) {
//    return genericController.sendFailure(null, getError(exception), HttpStatus.INTERNAL_SERVER_ERROR.value(),
//        request);
//  }
//
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<ResponseBO<Object>> handleException(Exception exception,
//      HttpServletRequest request) {
//
//    return genericController.sendFailure(null, getError(exception),
//        HttpStatus.INTERNAL_SERVER_ERROR.value(), request);
//  }
//
//  @ExceptionHandler(MissingServletRequestParameterException.class)
//  public ResponseEntity<ResponseBO<Object>> handleRuntimeException(
//      MissingServletRequestParameterException exception,
//      HttpServletRequest request) {
//    return genericController.sendFailure(null, getError(exception), HttpStatus.BAD_REQUEST.value(), request);
//  }
//
//  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//  public ResponseEntity<ResponseBO<Object>> handleHttpMediaTypeNotSupportedException(
//      HttpMediaTypeNotSupportedException exception, HttpServletRequest request) {
//    return genericController.sendFailure(null,getError(exception),
//        HttpStatus.BAD_REQUEST.value(),
//        request);
//  }
//
//  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//  public ResponseEntity<ResponseBO<Object>> handleMethodArgumentTypeMismatchException(
//      MethodArgumentTypeMismatchException methodArgumentTypeMismatchException,
//      HttpServletRequest request) {
//
//    return genericController.sendFailure(null,getError(methodArgumentTypeMismatchException),
//        HttpStatus.BAD_REQUEST.value(), request);
//  }
//
//  private ErrorBo getError(Exception exception) {
//    return ErrorBo.builder().errorCode("10001")
//        .errorMessage(exception.getMessage()).build();
//  }
//}