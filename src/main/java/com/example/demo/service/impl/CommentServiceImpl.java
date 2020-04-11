package com.example.demo.service.impl;

import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 根据ID拿到评论
     */
    @Override
    public Comment getComment(Long id) {
        return commentMapper.getComment(id);
    }

    /**
     * 拿到这篇博客的所有评论，并且根据父子级设置评论等级
     */
    @Override
    public List<Comment> getCommentByBlogId(Long blogId) {
        List<Comment> comments = commentMapper.getCommentByBlogId(blogId);
        // 根据创建时间排序评论
        Collections.sort(comments, Comparator.comparing(Comment::getCreateTime));

        for (Comment comment : comments) {
            Comment parent = commentMapper.getParentCommentId(comment);
            if (parent != null) {
                Long parentCommentId = parent.getId();
                // 设置父评论
                comment.setParentComments(commentMapper.getComment(parentCommentId));
                // 设置回复列表
                comment.setReplyComments(commentMapper.getCommentByParentId(parentCommentId));
            }
        }

        comments = loopEachComment(comments);

        return comments;
    }

    /**
     * 完成分级，将评论分为两级
     * 不对外暴露
     */
    private List<Comment> loopEachComment(List<Comment> comments) {
        List<Comment> topList = new ArrayList<>();
        // 拿到所有的顶级回复
        for (Comment comment : comments) {
            Comment c = comment;
            topList.add(c);
        }
        combineChildren(topList);
        return topList;
    }

    // 全局变量，用于评论分级
    private volatile List<Comment> tempReplies = new ArrayList<>();

    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            // 拿到每个Comment的回复列表
            List<Comment> replies = comment.getReplyComments();
            if (replies == null) continue;
            // 循环找到回复列表中的所有子回复，并存入一个tempReplies
            for (Comment r : replies) {
                recursively(r);
            }
            comment.setReplyComments(tempReplies);
            // 置空
            tempReplies = new ArrayList<>();
        }
    }
    /**
     * 递归把所有的子回复都拿到
     */
    private void recursively(Comment comment) {
        tempReplies.add(comment);
        // 拿到当前评论的所有子评论
        List<Comment> replyComments = comment.getReplyComments();
        if (replyComments != null && replyComments.size() > 0) {
            // 对每个评论递归找子评论
            for (Comment reply : replyComments) {
                tempReplies.add(reply);
                if (reply.getReplyComments() != null && reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }

    /**
     * 保存评论
     */
    @Override
    public boolean saveComment(Comment comment) {
        Comment parentComments = comment.getParentComments();

        if (parentComments == null) {
            comment.setParentComments(null);
        } else {    // 需要的话，设置父评论
            Comment parent = commentMapper.getComment(parentComments.getId());
            comment.setParentComments(parent);
        }

        comment.setCreateTime(new Date());
        return commentMapper.saveComment(comment);
    }
}
