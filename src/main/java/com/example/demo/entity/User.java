package com.example.demo.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class User {
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Integer type;   // 用户类型，目前只有管理员
    private Date createTime;
    private Date updateTime;

    // 外键关系 one 2 many
    private List<Blog> blogs;
}
