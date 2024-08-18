package com.bo.usercenter.model.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求参数统一封装类
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -980655476539306328L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

}
