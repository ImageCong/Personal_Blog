package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    // 登陆验证
    User checkUser(String username, String password);
}
