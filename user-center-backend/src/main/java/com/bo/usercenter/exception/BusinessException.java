package com.bo.usercenter.exception;


import com.bo.usercenter.common.ErrorCode;
import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 4953018605664021469L;
    private final int code;
    private final String discription;

    public BusinessException(String message, int code, String discription) {
        super(message);
        this.code = code;
        this.discription = discription;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.discription = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode,String discription) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.discription = discription;
    }

}
