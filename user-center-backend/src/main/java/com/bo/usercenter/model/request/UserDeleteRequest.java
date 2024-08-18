package com.bo.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户删除参数统一封装类型
 */
@Data
public class UserDeleteRequest implements Serializable {

    private static final long serialVersionUID = 5618654459337105249L;

    private long id;
}
