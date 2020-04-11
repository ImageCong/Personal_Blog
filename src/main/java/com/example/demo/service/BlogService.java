package com.example.demo.service;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Tag;
import com.example.demo.entity.Type;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Blog getBlogAndConvert(Long id);

    PageInfo<Blog> listBlog(String title, Type type, boolean recommend);

    boolean saveBlog(Blog blog);

    Blog getBlogByTitle(String title);

    List<Blog> listRecommendBlog();

    Map<String,List<Blog>> archivesBlogs();

    boolean updateBlog(Long id, Blog blog);

    boolean deleteBlog(Long id);

    List<Tag> listTags(Long id);

    List<Blog> getBlogByTypeId(Long id);

    int countBlogs();
}
