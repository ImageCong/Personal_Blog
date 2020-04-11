package com.example.demo.service;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogTagsService {
    /**
     * 查到此博客的所有标签
     */
    List<Tag> findAllTags(@Param("blog") Blog blog);

    /**
     * 查到具有此标签的所有博客
     */
    List<Blog> findAllBlog(@Param("tag") Tag tag);

    /**
     * 根据博客id保存标签
     */
    boolean saveBlogsAndTags(@Param("blog") Blog blog, @Param("tag") Tag tag);

    /**
     * 删除博客时，删除与标签的关系
     */
    boolean deleteRealtionTags(@Param("blog")Blog blog);

    /**
     * 删除标签时，删除与博客的关系
     */
    boolean deleteRealtionBlogs(@Param("tag") Tag tag);
}
