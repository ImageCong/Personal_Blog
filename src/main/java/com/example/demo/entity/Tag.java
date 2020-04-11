package com.example.demo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class Tag {
    private Long id;
    @NotBlank(message = "标签名称不能为空")
    private String name;

    // 外键关系 many 2 many
    private List<Blog> blogs;
}
