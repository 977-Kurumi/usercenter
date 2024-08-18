package com.bo.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bo.usercenter.model.domain.User;
import com.bo.usercenter.model.request.UserUpadteRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 24822
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-08-13 20:01:22
 * <p>
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param request session
     * @return 返回脱敏后的用户信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户查询
     * @param username 用户名
     * @return 查询出的用户信息以List形式返回
     */
    List<User> userSearch(String username);

    /**
     * 用户删除
     * @param id 用户名
     * @return 是否查询成功 true or false
     */
    boolean userDelete(long id);

    /**
     * 用户脱敏
     * @param originUser originUser
     * @return User
     */
    User getSafetyUser(User originUser);

    int userLogout(HttpServletRequest request);
//
//    int userUpdate(UserUpadteRequest userUpadteRequest);


}

