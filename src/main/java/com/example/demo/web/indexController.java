package com.example.demo.web;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Tag;
import com.example.demo.entity.Type;
import com.example.demo.service.BlogService;
import com.example.demo.service.BlogTagsService;
import com.example.demo.service.TagService;
import com.example.demo.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class indexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogTagsService blogTagsService;

    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1") Integer pageNum
            , @RequestParam(defaultValue = "8") Integer pageSize) {


        PageHelper.startPage(pageNum, pageSize);

        PageInfo<Blog> pageInfo = blogService.listBlog(null, null, false);

        //获得当前页
        model.addAttribute("pageNum", pageInfo.getPageNum());
        //获得一页显示的条数
        model.addAttribute("pageSize", pageSize);
        //是否是第一页
        model.addAttribute("isFirstPage", pageInfo.isIsFirstPage());
        //获得总页数
        model.addAttribute("totalPages", pageInfo.getPages());
        //是否是最后一页
        model.addAttribute("isLastPage", pageInfo.isIsLastPage());

        // 博客列表
        model.addAttribute("page", pageInfo.getList());
        // 博客总数
        model.addAttribute("size", pageInfo.getList().size());

        // 拿到类型列表并排序
        List<Type> typeList = typeService.listType().getList();
        for (Type t : typeList) {
            t.setBlogs(blogService.getBlogByTypeId(t.getId()));
        }
        Collections.sort(typeList, new Comparator<Type>() {
            @Override
            public int compare(Type o1, Type o2) {
                return o2.getBlogs().size() - o1.getBlogs().size();
            }
        });
        model.addAttribute("types", typeList);

        // 拿到标签列表并排序
        List<Tag> tagList = tagService.listTag().getList();
        for (Tag t : tagList) {
            t.setBlogs(blogTagsService.findAllBlog(t));
        }
        Collections.sort(tagList, new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o2.getBlogs().size() - o1.getBlogs().size();
            }
        });
        model.addAttribute("tags", tagList);

        // 拿到推荐博客列表
        model.addAttribute("recommendBlogs", blogService.listRecommendBlog());

        return "index";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam(defaultValue = "1") Integer pageNum
            , @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam String query) {

        PageHelper.startPage(pageNum, pageSize);

        PageInfo<Blog> pageInfo = blogService.listBlog(query, null, false);

        //获得当前页
        model.addAttribute("pageNum", pageInfo.getPageNum());
        //获得一页显示的条数
        model.addAttribute("pageSize", pageSize);
        //是否是第一页
        model.addAttribute("isFirstPage", pageInfo.isIsFirstPage());
        //获得总页数
        model.addAttribute("totalPages", pageInfo.getPages());
        //是否是最后一页
        model.addAttribute("isLastPage", pageInfo.isIsLastPage());

        // 博客列表
        model.addAttribute("page", pageInfo.getList());
        // 博客总数
        model.addAttribute("size", pageInfo.getList().size());

        model.addAttribute("queryTitle", query);

        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blogInfo(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlogAndConvert(id);

        // 由于blogTagsService只拿到了ID 还需要去拿name才能展示出来
        List<Tag> allTags = blogTagsService.findAllTags(blog);
        for (Tag t : allTags) {
            t.setName(tagService.getTag(t.getId()).getName());
        }
        blog.setTags(allTags);
        model.addAttribute("blog", blog);
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        List<Blog> blogs = blogService.listRecommendBlog();
        if (blogs.size() > 3) {
            blogs = blogs.subList(0, 3);
        }

        model.addAttribute("newblogs", blogs);
        return "_fragments :: newblogList";
    }
}
