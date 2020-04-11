package com.example.demo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.beans.Transient;
import java.util.Date;
import java.util.List;

@Data
public class Blog {
    private Long id;        // 博客唯一id
    @NotBlank(message = "标题不能为空")
    private String title;   // 标题
    @NotBlank(message = "内容不能为空")
    private String content; // 内容
    private String firstPicture;    //首图
    private String flag;    // 标记
    private Integer views;  // 浏览次数
    private boolean appreciation;   // 是否开启赞赏功能
    private boolean shareStatement; // 版权开启
    private boolean commentabled;   // 评论开启
    private boolean published;      // 发布状态
    private boolean recommend;      // 是否推荐
    private Date createTime;    // 创建时间
    private Date updateTime;    // 更新时间
    @NotBlank(message = "描述不能为空")
    private String description; // 描述

    // 外键关系 many 2 one
    private Type type;
    // 外键关系 many 2 many
    private List<Tag> tags;
    // 外键关系 many 2 one
    private User user;
    // 外键关系 one 2 many
    private List<Comment> comments;

    // 不进入DB，用于修改博客时，前台tags的获取
    transient private String tagIds;

    // 拿到tagIds并转为String类型，以逗号隔开
    public void initTagIds(List<Tag> tags) {
        if (tags == null) return;

        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            this.tagIds = ids.toString();
        }
    }
}
