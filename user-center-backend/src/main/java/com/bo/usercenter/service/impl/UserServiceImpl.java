package com.bo.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bo.usercenter.common.ErrorCode;
import com.bo.usercenter.exception.BusinessException;
import com.bo.usercenter.model.domain.User;
import com.bo.usercenter.model.request.UserUpadteRequest;
import com.bo.usercenter.service.UserService;
import com.bo.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.bo.usercenter.contant.UserContant.USER_LOGIN_STATE;

/**
 * @author 24822
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-08-13 20:01:22
 *
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 密码加密的加盐
     */
    private static final String SALT = "977";

    /**
     * 用户登录状态键
     */


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1. 校验
        if(StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"用户账号小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"用户密码过短");
        }


        // 校验账户不能包含特殊字符
        String validPattern = "\\p{P}|\\p{S}|\\s+";
        Pattern pattern = Pattern.compile(validPattern);
        Matcher matcher = pattern.matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"账户不能包含特殊字符");
        }

        // 密码与校验密码不相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"密码与校验密码不相同");
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"账户重复");
        }

        //2. 密码加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        boolean save = this.save(user);
        if(!save){
            return -1;
        }
        return user.getId();
    }



    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //1. 校验
        if(StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"账号密码为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"账户长度小于4");
        }
        if (userPassword.length() < 8 ){
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"密码长度小于8");
        }

        // 校验账户不能包含特殊字符
        String validPattern = "\\p{P}|\\p{S}|\\s+";
        Pattern pattern = Pattern.compile(validPattern);
        Matcher matcher = pattern.matcher(userAccount);
        if(matcher.find()){
            return null;
        }

        //2. 密码加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", newPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null){
            log.info("user login failed, userAccount can not match userPassword");
            throw new BusinessException(ErrorCode.PRAMS_ERROR,"用户不存在或账户密码错误");
        }

        //3.用户脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录用户的登陆状态在session中
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        //返回安全的脱敏后的信息
        return safetyUser;
    }

    @Override
    public List<User> userSearch(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username);
        }
        List<User> userList = userMapper.selectList(queryWrapper);

        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public boolean userDelete(long id) {
        if(id<=0){
            return false;
        }
        return userMapper.deleteById(id) > 0;
    }

    /**
     * 用户信息脱敏
     * @param originUser User实体类
     * @return safetyUser
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {

        //session操作类似一个map
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

//    public int userUpdate(UserUpadteRequest userUpadteRequest) {
//
//        //session操作类似一个map
//        User user = new User();
//        user.setUsername(userUpadteRequest.getUsername());
//        user.setUserAccount(userUpadteRequest.getUserAccount());
//        user.setAvatarUrl(userUpadteRequest.getAvatarUrl());
//        user.setGender(userUpadteRequest.getGender());
//        user.setPhone(userUpadteRequest.getPhone());
//        user.setEmail(userUpadteRequest.getEmail());
//        user.setUserStatus(userUpadteRequest.getUserStatus());
//        user.setUserRole(userUpadteRequest.getUserRole());
//
//        return userMapper.updateById(user);
//    }
}





