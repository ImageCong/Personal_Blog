package com.example.demo.service;

import com.example.demo.entity.Tag;
import com.example.demo.exception.BindingException;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TagService {
    boolean saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    PageInfo<Tag> listTag();

    List<Tag> listTagWithCondition(String ids);

    boolean updateTag(Long id, Tag tag);

    boolean deleteTag(Long id) throws BindingException;
}
