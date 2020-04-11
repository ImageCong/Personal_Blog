package com.example.demo.web;

import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.service.BlogService;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String toCommentList(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.getCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String postComment(@Valid Comment comment, HttpSession session) {

        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        comment.setParentComments(comment.getParentComments());

        User user = (User) session.getAttribute("user");
        if (user == null) {
            comment.setAvatar(avatar);
            comment.setAdminComment(false);
        } else {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            comment.setNickname(user.getNickname());
        }
        commentService.saveComment(comment);

        return "redirect:/comments/" + blogId;
    }
}
