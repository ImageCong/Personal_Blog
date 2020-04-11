package com.example.demo.service.impl;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Tag;
import com.example.demo.entity.Type;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.BlogMapper;
import com.example.demo.mapper.BlogTagsMapper;
import com.example.demo.mapper.TagMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.BlogService;
import com.example.demo.service.BlogTagsService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BlogTagsServiceImpl implements BlogTagsService {

    @Autowired
    private BlogTagsMapper blogTagsMapper;

    @Override
    public List<Tag> findAllTags(Blog blog) {
        return blogTagsMapper.findAllTags(blog);
    }
    @Override
    public List<Blog> findAllBlog(Tag tag) {
        return blogTagsMapper.findAllBlog(tag);
    }
    @Override
    public boolean saveBlogsAndTags(Blog blog, Tag tag) {
        return saveBlogsAndTags(blog, tag);
    }
    @Override
    public boolean deleteRealtionTags(Blog blog) {
        return blogTagsMapper.deleteRealtionTags(blog);
    }
    @Override
    public boolean deleteRealtionBlogs(Tag tag) {
        return blogTagsMapper.deleteRealtionBlogs(tag);
    }
}
