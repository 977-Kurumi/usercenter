package com.bo.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求参数统一封装类
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 5598324286237299843L;

    private String userAccount;

    private String userPassword;

}
