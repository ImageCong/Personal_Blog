package com.example.demo.web;

import com.example.demo.entity.Type;
import com.example.demo.service.BlogService;
import com.example.demo.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class TypeShowController {
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String type(Model model, @RequestParam(defaultValue = "1") Integer pageNum
            , @RequestParam(defaultValue = "5") Integer pageSize, @PathVariable Long id) {

        PageHelper.startPage(pageNum, pageSize);

        PageInfo<Type> pageInfo = typeService.listType();
        List<Type> typeList = pageInfo.getList();
        if (id == -1) {
            id = typeList.get(0).getId();
        }

        for (Type t : typeList) {
            t.setBlogs(blogService.getBlogByTypeId(t.getId()));
        }
        Collections.sort(typeList, new Comparator<Type>() {
            @Override
            public int compare(Type o1, Type o2) {
                return o2.getBlogs().size() - o1.getBlogs().size();
            }
        });
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
        model.addAttribute("types", typeList);
        // 拿到博客
        model.addAttribute("page", blogService.listBlog(null, typeService.getType(id), false).getList());
        // ID回传
        model.addAttribute("activeTypeId", id);

        return "types";
    }
}
