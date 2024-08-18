package com.bo.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bo.usercenter.model.domain.User;

/**
* @author 24822
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-08-13 20:01:22
* @Entity com.bo.usercenter.model.domain.User
 *
 * 其实就是mybatis中的DAO层
*/
public interface UserMapper extends BaseMapper<User> {

}




