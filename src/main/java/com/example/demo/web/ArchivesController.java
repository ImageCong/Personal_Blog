package com.example.demo.web;

import com.example.demo.entity.Blog;
import com.example.demo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ArchivesController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String achieve(Model model) {
        Map<String, List<Blog>> archiveMap = blogService.archivesBlogs();
        int countBlogs = blogService.countBlogs();
        model.addAttribute("archiveMap", archiveMap);
        model.addAttribute("blogCount", countBlogs);
        return "archives";
    }
}
