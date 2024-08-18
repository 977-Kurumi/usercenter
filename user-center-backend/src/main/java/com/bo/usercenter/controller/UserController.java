package com.bo.usercenter.controller;


import com.bo.usercenter.common.BaseResponse;
import com.bo.usercenter.common.ErrorCode;
import com.bo.usercenter.common.ResultUtils;
import com.bo.usercenter.exception.BusinessException;
import com.bo.usercenter.model.domain.User;
import com.bo.usercenter.model.request.*;
import com.bo.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.bo.usercenter.contant.UserContant.ADMIN_ROLE;
import static com.bo.usercenter.contant.UserContant.USER_LOGIN_STATE;

/**
 * 用户接口
 */

//RestController注解适用于编写restful风格的api 返回值默认为json
//RESTful，一种通用的前后台交互方式
//RESTful一般是指这种格式：
//（1）使用HTTP POST(或GET)进行数据交互
//（2）请求数据和应答数据均为JSON格式（或XML）
@RestController
@RequestMapping("/user")
//RequestMapping用于定义请求路径
public class UserController {

    @Resource
    private UserService userService;

    /**
     * @param userRegisterRequest 用户注册请参数求统一封装类
     * @return 用户id
     */
    @PostMapping("/register")
    //@RequestBody 注解用于将前端传来的json参数与请求参数对象做关联
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        //校验userRegisterRequest请求参数
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //对请求参数本身进行校验，不涉及业务逻辑
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"请求参数错误");
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录接口
     *
     * @param userLoginRequest 统一登录参数封装类
     * @param request          Http请求
     * @return user实体类
     */
    @PostMapping("/login")
    //@RequestBody 注解用于将前端传来的json参数与请求参数对象做关联
    public BaseResponse<User> useLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        //校验userRegisterRequest请求参数
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"请求参数错误");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        //对请求参数本身进行校验，不涉及业务逻辑
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"请求参数错误");
        }
        User user = userService.doLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request HttpServletRequest
     * @return int
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> useLogout(HttpServletRequest request) {
        //校验userRegisterRequest请求参数
        if (request == null) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"请求参数错误");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取用户登录态
     *
     * @param request HttpServletRequest
     * @return User
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObject;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH,"未登录");
        }
        long userId = currentUser.getId();
        //TODO 校验用户状态是否合法(是否被封号等)
        User byId = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(byId);
        return ResultUtils.success(safetyUser);
    }

    @PostMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestBody UserSearchRequest userSearchRequest, HttpServletRequest request) {
        //鉴权
        //获取session 通过定义的key:USER_LOGIN_STATE
        //仅管理员可查
        if (userSearchRequest == null) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"请求参数错误");
        }
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"无权限");
        }
        List<User> userList = userService.userSearch(userSearchRequest.getUsername());
        return ResultUtils.success(userList);
    }

    /**
     * 修改用户信息
     * @param userUpadteRequest userUpadteRequest
     * @param request request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpadteRequest userUpadteRequest, HttpServletRequest request) {
        //鉴权
        //获取session 通过定义的key:USER_LOGIN_STATE
        //仅管理员可修改
        if (userUpadteRequest == null) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"请求参数错误");
        }
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"无权限");
        }
        User user = new User();
        BeanUtils.copyProperties(userUpadteRequest, user);
        long id = userUpadteRequest.getId();
        // 判断是否存在
        User oldUser = userService.getById(id);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"无");
        }
        boolean result = userService.updateById(user);
        if (result){
            return ResultUtils.success(result);
        }else {
            return ResultUtils.noSuccess(result);
        }
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request) {
        //鉴权
        //获取session 通过定义的key:USER_LOGIN_STATE
        if (userDeleteRequest == null) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"请求参数错误");
        }
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"无权限");
        }
        boolean result = userService.userDelete(userDeleteRequest.getId());
        if (result){
            return ResultUtils.success(result);
        }else {
            return ResultUtils.noSuccess(result);
        }

    }


    /**
     * 是否为管理员
     *
     * @param request Http请求
     * @return boolean型是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        //鉴权
        //获取session 通过定义的key:USER_LOGIN_STATE
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        return user == null || user.getUserRole() != ADMIN_ROLE;
    }
}
