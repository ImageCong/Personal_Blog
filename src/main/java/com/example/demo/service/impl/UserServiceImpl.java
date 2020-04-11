package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     */
    @Override
    public User checkUser(String username, String password) {
        User user = userMapper.checkUser(username, password);
        if (user != null){
            log.info("auth root ============"+ user.getUsername());
        }else {
            log.info("password incorrect ============= login fail");
        }
        return user;
    }
}
