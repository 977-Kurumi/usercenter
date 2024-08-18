package com.bo.usercenter.model.request;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户查询参数统一封装类型
 */
@Data
public class UserUpadteRequest implements Serializable {


    private static final long serialVersionUID = -4506842022081663907L;

    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0为正常
     */
    private Integer userStatus;


    private Integer userRole;

}
