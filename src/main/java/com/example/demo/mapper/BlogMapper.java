package com.example.demo.mapper;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Type;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogMapper {
    /**
     * 获取博客
     */
    Blog getBlog(Long id);

    /**
     * 条件查询
     * 实现了分页
     * 支持title、type、recommend三者的组合查询
     */
    List<Blog> listBlog(@Param("title") String title, @Param("type") Type type, @Param("recommend") boolean recommend);

    /**
     * 列出推荐的博客
     */
    List<Blog> listRecommendBlog();

    /**
     * 博客保存
     */
    boolean saveBlog(@Param("blog") Blog blog);

    /**
     * 根据博客名找到博客
     */
    Blog getBlogByTitle(String title);

    /**
     * 更新博客内容
     */
    int updateBlog(@Param(value = "id") Long id, @Param(value = "blog") Blog blog);

    /**
     * 删除博客
     */
    boolean deleteBlog(Long id);

    /**
     * 拿到所有的日期
     */
    List<String> getCreateTime();

    /**
     * 根据日期拿到博客
     */
    List<Blog> listBlogByCreateTime(@Param("date")String date);

    /**
     * 更新浏览次数
     */
    int updateViews(Long id);

    /**
     * 根据typeid查博客
     */
    List<Blog> getBlogByTypeId(Long id);

    /**
     * 统计博客总数
     */
    int countBlogs();
}
