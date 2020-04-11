package com.example.demo.service;

import com.example.demo.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService {

    Comment getComment(Long id);

    List<Comment> getCommentByBlogId(Long blogId);

    boolean saveComment(Comment comment);
}
