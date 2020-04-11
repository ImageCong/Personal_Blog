package com.example.demo.service.impl;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Type;
import com.example.demo.exception.BindingException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.BlogMapper;
import com.example.demo.mapper.TypeMapper;
import com.example.demo.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private BlogMapper blogMapper;
    /**
     * 保存类型
     */
    @Override
    @Transactional
    public boolean saveType(Type type) {
        if (getTypeByName(type.getName()) != null) {
            throw new NotFoundException("类型已存在");
        }
        int result = typeMapper.saveType(type);
        if (result != 1) return false;
        else return true;
    }

    /**
     * 拿到类型
     */
    @Override
    public Type getType(Long id) {
        return typeMapper.getType(id);
    }

    /**
     * 根据类型名拿到类型
     */
    @Override
    public Type getTypeByName(String name) {
        return typeMapper.getTypeByName(name);
    }

    /**
     * 拿到所有的类型
     */
    @Override
    @Transactional
    public PageInfo<Type> listType() {
        List<Type> types = typeMapper.listType();

        PageInfo<Type> pageInfo = new PageInfo<>(types);

        return pageInfo;
    }

    /**
     * 更新类型名
     */
    @Override
    @Transactional
    public boolean updateType(Long id, Type type) {
        // 确保DB中存在这个Type标签
        Type ty = typeMapper.getType(id);
        if (ty == null) throw new NotFoundException("不存在该类型");

        int result = typeMapper.updateType(id, type);
        if (result != 1) return false;
        else return true;
    }

    /**
     * 删除类型
     */
    @Override
    @Transactional
    public boolean deleteType(Long id) throws BindingException {
        List<Blog> blogByTypeId = blogMapper.getBlogByTypeId(id);
        if (blogByTypeId != null && blogByTypeId.size() > 0) {
            log.warn("此标签已有对应的博客绑定，请修改后再删除");
            throw new BindingException("有博客绑定中，不能删除");
        }

        int result = typeMapper.deleteType(id);
        if (result != 1) return false;
        else return true;
    }
}
