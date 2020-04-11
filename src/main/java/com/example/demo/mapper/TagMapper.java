package com.example.demo.mapper;

import com.example.demo.entity.Tag;
import com.example.demo.entity.Type;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {
    /**
     * 根据id获取标签
     */
    Tag getTag(@Param(value = "id") Long id);

    /**
     * 根据标签名获取标签
     */
    Tag getTagByName(String name);

    /**
     * 删除标签
     */
    int deleteTag(@Param(value = "id") Long id);

    /**
     * 新增标签
     */
    int saveTag(@Param(value = "tag") Tag tag);

    /**
     * 列出已有标签
     */
    List<Tag> listTag();


    /**
     * 根据ID列出tags
     */
    List<Tag> listTagWithCondition(@Param(value = "ids") Long[] ids);

    /**
     * 更新标签名
     */
    int updateTag(@Param(value = "id") Long id, @Param(value = "tag") Tag tag);
}
