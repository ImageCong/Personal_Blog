package com.example.demo.web.admin;

import com.example.demo.entity.Type;
import com.example.demo.exception.BindingException;
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

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 查询类型，以分页的方式展现
     */
    @GetMapping("/types")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer pageNum
            , @RequestParam(defaultValue = "5") Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        PageInfo<Type> pageInfo = typeService.listType();

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
        model.addAttribute("page", pageInfo.getList());

        return "admin/types";
    }

    /**
     * 新增类型跳转
     */
    @GetMapping("types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    /**
     * 跳转到编辑类型页面
     */
    @GetMapping("types/{id}/input")
    public String eridInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));

        return "admin/types-input";
    }

    /**
     * 真正的新增类型
     */
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult bindingResult, RedirectAttributes attributes) {
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName != null) {
            bindingResult.rejectValue("name", "nameError", "type already exist");
        }

        if (bindingResult.hasErrors()) {
            log.info("ERROR");
            return "admin/types-input";
        }

        boolean isSave = typeService.saveType(type);

        if (isSave == false) {
            attributes.addFlashAttribute("message", "新增失败");
            log.info("save fail =================");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
            log.warn("save suceess ==================");
        }
        return "redirect:/admin/types";
    }

    /**
     * 修改类型，可以和新增合并来优化
     * 在新增中判断id是否存在即可
     */
    @PostMapping("/types/{id}")
    public String edisPost(@Valid Type type, BindingResult bindingResult,
                           @PathVariable Long id, RedirectAttributes attributes) {
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName != null) {
            bindingResult.rejectValue("name", "nameError", "已存在此类型");
        }

        if (bindingResult.hasErrors()) {
            log.info("ERROR");
            return "admin/types-input";
        }

        boolean isSave = typeService.updateType(id, type);

        if (isSave == false) {
            attributes.addFlashAttribute("message", "更新失败");
            log.warn("update fail ==================");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
            log.warn("update suceess ==================");
        }
        return "redirect:/admin/types";
    }

    /**
     * 删除类型
     */
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            typeService.deleteType(id);
        } catch (BindingException e) {
            attributes.addFlashAttribute("error", "此类型已经被博客绑定，请先修改后再删除");
            return "redirect:/admin/types";
        }
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }

}
