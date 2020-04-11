package com.example.demo.service.impl;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Tag;
import com.example.demo.entity.Type;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.BlogMapper;
import com.example.demo.mapper.BlogTagsMapper;
import com.example.demo.mapper.TagMapper;
import com.example.demo.service.BlogService;
import com.example.demo.util.MarkDownUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogTagsMapper blogTagsMapper;
    @Autowired
    TagMapper tagMapper;

    /**
     * 根据ID查到博客
     * admin的修改博客需要用到此方法
     */
    @Override
    public Blog getBlog(Long id) {
        return blogMapper.getBlog(id);
    }

    /**
     * 拿到博客，将markdown格式转为HTML
     * 博客详情页调用此方法
     */
    @Override
    @Transactional
    public Blog getBlogAndConvert(Long id) {
        Blog blog = getBlog(id);    // 拿到博客
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        String content = blog.getContent(); // 拿到内容
        content = MarkDownUtils.markdownToHtmlExtensions(content);  // 转换HTML
        blog.setContent(content);   // 设置内容
        blogMapper.updateViews(id); // 浏览次数+1
        return blog;
    }

    /**
     * 拿到博客列表，按照更新时间降序排序
     * 分页由上层做
     */
    @Override
    public PageInfo<Blog> listBlog(String title, Type type, boolean recommend) {
        List<Blog> blogs = blogMapper.listBlog(title, type, recommend);

        for (Blog b : blogs) {
            // 这里只拿到了tags的id，没有name
            List<Tag> allTags = blogTagsMapper.findAllTags(b);
            b.setTags(allTags);
        }
        Collections.sort(blogs, (x, y) -> y.getUpdateTime().compareTo(x.getUpdateTime()));

        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        return pageInfo;
    }

    /**
     * 新增博客
     *  新增博客时，在业务逻辑层设置创建时间、更新时间、浏览次数
     *  还需要再blog-tags表进行保存
     */
    @Override
    @Transactional
    public boolean saveBlog(Blog blog) {

        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);

        // 保存博客
        boolean flag = blogMapper.saveBlog(blog);

        // 拿到ID，在blog-tag表设置
        Long id = getBlogByTitle(blog.getTitle()).getId();
        blog.setId(id);

        List<Tag> tags = blog.getTags();
        for (Tag t : tags) {
            blogTagsMapper.saveBlogsAndTags(blog, t);
        }
        return flag;
    }

    /**
     * 根据title拿到博客，这里做的是全匹配，为的就是找到是否有重复的博客题目
     */
    @Override
    public Blog getBlogByTitle(String title) {
        return blogMapper.getBlogByTitle(title);
    }

    /**
     * 列出所有的推荐博客
     */
    @Override
    public List<Blog> listRecommendBlog() {
        return blogMapper.listRecommendBlog();
    }

    /**
     * 博客归档，按照年份排序
     */
    @Override
    public Map<String, List<Blog>> archivesBlogs() {
        Map<String, List<Blog>> map = new TreeMap<>((x, y) -> y.compareTo(x));
        List<String> dates = blogMapper.getCreateTime();
        for (String s : dates) {
            map.put(s, blogMapper.listBlogByCreateTime(s));
        }
        return map;
    }

    /**
     * 更新博客内容
     *  这里也需要更改blog-tag的关系
     */
    @Override
    @Transactional
    public boolean updateBlog(Long id, Blog blog) {
        // 先做一个验证
        Blog b = blogMapper.getBlog(id);
        if (b == null) throw new NotFoundException("该博客不存在");

        blog.setUpdateTime(new Date());
        int isUpdate = blogMapper.updateBlog(id, blog);

        // 首先删除所有的realtion
        blogTagsMapper.deleteRealtionTags(blog);
        List<Tag> tags = blog.getTags();
        System.out.println(tags);
        // 重新保存所有的relation
        for (Tag t : tags) {
            blogTagsMapper.saveBlogsAndTags(blog, t);
        }

        if (isUpdate == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除博客
     * 相应的需要删除对应的tag关系
     */
    @Override
    @Transactional
    public boolean deleteBlog(Long id) {
        Blog b = new Blog();
        b.setId(id);
        // 删除与之相关的tag
        blogTagsMapper.deleteRealtionTags(b);
        return blogMapper.deleteBlog(id);
    }

    /**
     * 拿到所有的tags
     */
    @Override
    public List<Tag> listTags(Long id) {
        Blog blog = new Blog();
        blog.setId(id);
        List<Tag> allTags = blogTagsMapper.findAllTags(blog);

        int size = allTags.size();
        while (size > 0) {
            Tag tag1 = allTags.get(0);
            Tag tag = tagMapper.getTag(tag1.getId());
            allTags.add(tag);
            size--;
            allTags.remove(tag1);
        }

        return allTags;
    }

    /**
     * 根据类型ID拿到博客
     */
    @Override
    public List<Blog> getBlogByTypeId(Long typeId) {
        return blogMapper.getBlogByTypeId(typeId);
    }

    /**
     * 统计博客总数
     */
    @Override
    public int countBlogs() {
        return blogMapper.countBlogs();
    }
}
