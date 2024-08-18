package com.bo.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户查询参数统一封装类型
 */
@Data
public class UserSearchRequest implements Serializable {

    private static final long serialVersionUID = -8652766245431208348L;

    private String username;
}
