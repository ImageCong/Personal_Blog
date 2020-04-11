package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /**
     * 用户登录
     */
    User checkUser(@Param(value = "username") String username, @Param(value = "password") String password);

    /**
     * 获取用户
     */
    User getUser(@Param(value = "id") Long id);
}
