package com.example.demo.web.admin;

import com.example.demo.entity.Tag;
import com.example.demo.entity.Type;
import com.example.demo.exception.BindingException;
import com.example.demo.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 查询标签，以分页的方式展现
     */
    @GetMapping("/tags")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer pageNum
            , @RequestParam(defaultValue = "5") Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        PageInfo<Tag> pageInfo = tagService.listTag();

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
        // 标签NotFoundException列表
        model.addAttribute("page", pageInfo.getList());

        return "admin/tags";
    }

    /**
     * 新增标签跳转
     */
    @GetMapping("tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Type());
        return "admin/tags-input";
    }

    /**
     * 跳转到编辑标签页面
     */
    @GetMapping("tags/{id}/input")
    public String eridInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    /**
     * 真正的新增标签
     * Valid和BindingResult一一对应
     * 用于进行校验
     */
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes attributes) {
        Tag tagByName = tagService.getTagByName(tag.getName());
        if (tagByName != null) {
            bindingResult.rejectValue("name", "nameError", "tag already exist");
        }

        if (bindingResult.hasErrors()) {
            log.info("ERROR");
            return "admin/tags-input";
        }

        boolean isSave = tagService.saveTag(tag);

        if (isSave == false) {
            attributes.addFlashAttribute("message", "新增失败");
            log.info("save fail =================");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
            log.warn("save suceess ==================");
        }
        return "redirect:/admin/tags";
    }

    /**
     * 修改标签，可以和新增合并来优化
     * 在新增中判断id是否存在即可
     */
    @PostMapping("/tags/{id}")
    public String edisPost(@Valid Tag tag, BindingResult bindingResult, @PathVariable Long id, RedirectAttributes attributes) {
        Tag tagByName = tagService.getTagByName(tag.getName());
        if (tagByName != null) {
            bindingResult.rejectValue("name", "nameError", "已存在此标签");
        }

        if (bindingResult.hasErrors()) {
            log.info("ERROR");
            return "admin/tags-input";
        }

        boolean isSave = tagService.updateTag(id, tag);

        if (isSave == false) {
            attributes.addFlashAttribute("message", "更新失败");
            log.warn("update fail ==================");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
            log.warn("update suceess ==================");
        }
        return "redirect:/admin/tags";
    }

    /**
     * 删除标签
     */
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            tagService.deleteTag(id);
        }catch (BindingException e){
            attributes.addFlashAttribute("error","此标签已经被博客绑定，请先修改后再删除");
            return "redirect:/admin/tags";
        }
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

}
