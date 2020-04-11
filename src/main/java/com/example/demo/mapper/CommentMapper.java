package com.example.demo.mapper;

import com.example.demo.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {

    /**
     * 根据ID获取评论
     */
    Comment getComment(@Param("id") Long id);

    /**
     * 拿到父评论ID
     */
    Comment getParentCommentId(@Param("comment") Comment comment);

    /**
     *  拿到所有的顶层评论
     */
    List<Comment> getCommentByBlogId(@Param("blogId") Long blogId);

    /**
     *  根据父评论ID拿到所有子评论
     */
    List<Comment> getCommentByParentId(@Param("parentId") Long parentId);

    /**
     * 保存评论
     */
    boolean saveComment(@Param("comment") Comment comment);
}
