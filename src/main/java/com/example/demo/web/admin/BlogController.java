package com.example.demo.web.admin;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Tag;
import com.example.demo.entity.User;
import com.example.demo.exception.BindingException;
import com.example.demo.service.BlogService;
import com.example.demo.service.TagService;
import com.example.demo.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /**
     * 跳转到管理员的Blog首页
     */
    @GetMapping("/blogs")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer pageNum
            , @RequestParam(defaultValue = "8") Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        PageInfo<Blog> pageInfo = blogService.listBlog(null, null, false);

        // 获得当前页
        model.addAttribute("pageNum", pageInfo.getPageNum());
        // 获得一页显示的条数
        model.addAttribute("pageSize", pageSize);
        // 是否是第一页
        model.addAttribute("isFirstPage", pageInfo.isIsFirstPage());
        // 获得总页数
        model.addAttribute("totalPages", pageInfo.getPages());
        // 是否是最后一页
        model.addAttribute("isLastPage", pageInfo.isIsLastPage());
        // 博客列表
        model.addAttribute("page", pageInfo.getList());
        // 拿到类型列表
        model.addAttribute("types", typeService.listType().getList());

        return "admin/blogs";
    }

    /**
     * 根据条件执行组合搜索
     * 目前有两个小问题
     * 1、当执行完查询后，分页失效了
     * 2、当执行查询后，假设有多页，前端进行翻页的时候不会保存page的查询结果（需要改成ajax）
     */
    @PostMapping("/blogs/search")
    public String search(Model model, @RequestParam(defaultValue = "1") Integer pageNum
            , @RequestParam(defaultValue = "8") Integer pageSize, @RequestParam(name = "title") String title,
                         @RequestParam(name = "typeId") Long typeId, @RequestParam(name = "recommend") boolean recommend) {
        PageHelper.startPage(pageNum, pageSize);

        PageInfo<Blog> pageInfo = blogService.listBlog(title, typeService.getType(typeId), recommend);

        // 获得当前页
        model.addAttribute("pageNum", pageInfo.getPageNum());
        // 获得一页显示的条数
        model.addAttribute("pageSize", pageSize);
        // 是否是第一页
        model.addAttribute("isFirstPage", pageInfo.isIsFirstPage());
        // 获得总页数
        model.addAttribute("totalPages", pageInfo.getPages());
        // 是否是最后一页
        model.addAttribute("isLastPage", pageInfo.isIsLastPage());
        // 博客列表
        model.addAttribute("page", pageInfo.getList());

        return "admin/blogs :: blogList";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("types", typeService.listType().getList());
        model.addAttribute("tags", tagService.listTag().getList());
        return "admin/blogs-input";
    }

    /**
     * 跳转到修改页面
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("types", typeService.listType().getList());

        List<Tag> tags = blogService.listTags(id);

        model.addAttribute("tags", tagService.listTag().getList());

        Blog blog = blogService.getBlog(id);
        // 初始化标签列表
        blog.initTagIds(tags);
        model.addAttribute("blog", blog);

        return "admin/blogs-input";
    }

    /**
     * 真正的新增和修改(方法共用)
     */
    @PostMapping("/blogs")
    public String post(@Valid Blog blog, BindingResult bindingResult,
                       RedirectAttributes attributes, HttpSession session) {
        boolean b = false;
        if (blog.getId() == null) { // 新增博客
            if (blogService.getBlogByTitle(blog.getTitle()) != null) {
                bindingResult.rejectValue("title", "nameErr", "已存在博客标题，请重新设置");
            }
            if (bindingResult.hasErrors()) {
                return "/admin/blogs-input";
            }

            blog.setUser((User) session.getAttribute("user"));

            if (blog.getType() != null)      // Type非空验证
                blog.setType(typeService.getType(blog.getType().getId()));
            if (blog.getTagIds() != null)   // Tags非空验证
                blog.setTags(tagService.listTagWithCondition(blog.getTagIds()));

            b = blogService.saveBlog(blog);
        } else {    // 修改博客
            if (blog.getTagIds() != "") {
                blog.setTags(tagService.listTagWithCondition(blog.getTagIds()));
            } else {// Tags非空验证
                throw new BindingException("tags不能为空");
            }

            List<Tag> tagList = tagService.listTagWithCondition(blog.getTagIds());
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == false) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }

}
