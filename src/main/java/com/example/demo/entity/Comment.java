package com.example.demo.entity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class Comment {
    private Long id;
    private String nickname;
    @Email
    private String email;
    @NotBlank(message = "评论不能为空")
    private String content; // 评论内容
    private String avatar;  // 头像，图片地址
    private Date createTime;    // 创建时间

    // 外键关系 many 2 one
    private Blog blog;
    // 外键关系 自关联 子评论
    private List<Comment> replyComments;
    // 外键关系 自关联 唯一父评论
    private Comment parentComments;

    private boolean adminComment;

}
