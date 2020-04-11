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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogTagsService blogTagsService;

    @GetMapping("/tags/{id}")
    public String type(Model model, @RequestParam(defaultValue = "1") Integer pageNum
            , @RequestParam(defaultValue = "5") Integer pageSize, @PathVariable Long id) {

        PageHelper.startPage(pageNum, pageSize);

        // 拿到了所有的tag，相关联的blog目前只有ID
        PageInfo<Tag> pageInfo = tagService.listTag();
        List<Tag> tagList = pageInfo.getList();

        if (id == -1) {
            id = tagList.get(0).getId();
        }

        for (Tag t : tagList) {
            // 拿到了标签关联的blogs
            List<Blog> blogs = t.getBlogs();
            if (blogs != null || blogs.size() > 0) {
                int size = blogs.size();
                while (size > 0) {
                    Blog b = blogs.get(0);
                    Blog tempBlog = blogService.getBlog(b.getId());
                    blogs.add(tempBlog);
                    size--;
                    blogs.remove(b);
                }
            }
            t.setBlogs(blogs);
        }
        Collections.sort(tagList, (x, y) -> y.getBlogs().size() - x.getBlogs().size());

        Long target = tagService.getTag(id).getId();
        int targetID = 0;
        for (int i=0;i<tagList.size();i++){
            if (tagList.get(i).getId() == target){
                targetID = i;
                break;
            }
        }

        // 获得当前页
        model.addAttribute("pageNum", pageInfo.getPageNum());
        // 获得一页显示的条数
        model.addAttribute("pageSize", pageInfo.getPageSize());
        // 是否是第一页
        model.addAttribute("isFirstPage", pageInfo.isIsFirstPage());
        // 获得总页数
        model.addAttribute("totalPages", pageInfo.getPages());
        // 是否是最后一页
        model.addAttribute("isLastPage", pageInfo.isIsLastPage());
        // 类型列表
        model.addAttribute("tags", tagList);
        // 拿到博客
        model.addAttribute("page", tagList.get(targetID).getBlogs());
        // ID回传
        model.addAttribute("activeTagId", id);

        return "tags";
    }
}
