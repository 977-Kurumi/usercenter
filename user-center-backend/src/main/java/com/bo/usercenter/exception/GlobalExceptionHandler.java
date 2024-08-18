package com.bo.usercenter.exception;


import com.bo.usercenter.common.BaseResponse;
import com.bo.usercenter.common.ErrorCode;
import com.bo.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)//注解中参数作用为只去捕获BusinessException这个类型的异常

    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage(),e.getDiscription());
    }

    @ExceptionHandler(RuntimeException.class)//注解中参数作用为只去捕获BusinessException这个类型的异常
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(),"");
    }
}
