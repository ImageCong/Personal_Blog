package com.example.demo.mapper;

import com.example.demo.entity.Type;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TypeMapper {
    /**
     * 根据id获取类型
     */
    Type getType(@Param(value = "id") Long id);

    /**
     * 根据类型名获取类型
     */
    Type getTypeByName(String name);

    /**
     * 删除类型
     */
    int deleteType(@Param(value = "id") Long id);

    /**
     * 新增类型
     */
    int saveType(@Param(value="type")Type type);

    /**
     * 列出已有类型
     */
    List<Type> listType();

    /**
     * 更新类型名
     */
    int updateType(@Param(value = "id") Long id, @Param(value = "type") Type type);
}
