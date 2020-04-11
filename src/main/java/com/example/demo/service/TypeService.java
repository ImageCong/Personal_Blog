package com.example.demo.service;

import com.example.demo.entity.Type;
import com.example.demo.exception.BindingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TypeService {
    boolean saveType(Type type);

    Type getType(Long id);

    Type getTypeByName(String name);

    PageInfo<Type> listType();

    boolean updateType(Long id, Type type);

    boolean deleteType(Long id) throws BindingException;
}
