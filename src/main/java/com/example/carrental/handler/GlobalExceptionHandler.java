package com.example.carrental.handler;

import com.example.carrental.common.ApiResponse;
import com.example.carrental.common.BusinessException;
import com.example.carrental.common.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
/**
 * 全局异常处理器。
 * 统一把业务异常、参数校验异常和系统异常转换成标准响应格式。
 */
public class GlobalExceptionHandler {

    /**
     * 处理业务异常。
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusiness(BusinessException ex) {
        return ApiResponse.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理参数校验异常。
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class
    })
    public ApiResponse<Void> handleValidation(Exception ex) {
        return ApiResponse.fail(ErrorCode.BAD_REQUEST, ex.getMessage());
    }

    /**
     * 处理未预期异常。
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex) {
        return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, ex.getMessage());
    }
}
