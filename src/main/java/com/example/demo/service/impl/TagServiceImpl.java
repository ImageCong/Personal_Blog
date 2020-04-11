package com.example.demo.service.impl;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Tag;
import com.example.demo.exception.BindingException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.BlogTagsMapper;
import com.example.demo.mapper.TagMapper;
import com.example.demo.service.TagService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogTagsMapper blogTagsMapper;

    /**
     * 新增一个标签
     */
    @Override
    @Transactional
    public boolean saveTag(Tag tag) {
        int result = tagMapper.saveTag(tag);
        if (result != 1) return false;
        else return true;
    }

    /**
     * 拿到一个标签
     */
    @Override
    public Tag getTag(Long id) {
        return tagMapper.getTag(id);
    }

    /**
     * 根据标签名拿到标签
     */
    @Override
    public Tag getTagByName(String name) {
        return tagMapper.getTagByName(name);
    }

    /**
     * 列出所有的标签
     * 注意这里的blog只有id
     */
    @Override
    @Transactional
    public PageInfo<Tag> listTag() {
        List<Tag> tags = tagMapper.listTag();

        for (Tag t : tags) {
            // 这里只拿到了blog的id，没有其他信息
            List<Blog> allBlog = blogTagsMapper.findAllBlog(t);
            t.setBlogs(allBlog);
        }

        PageInfo<Tag> pageInfo = new PageInfo<>(tags);

        return pageInfo;
    }

    /**
     * 根据选定的id查到对应的标签
     */
    @Override
    public List<Tag> listTagWithCondition(String ids) {
        String[] strs = ids.split(",");
        Long[] idArrs = new Long[strs.length];
        for (int i = 0; i < idArrs.length; i++) {
            idArrs[i] = Long.valueOf(strs[i]);
        }
        List<Tag> tags = tagMapper.listTagWithCondition(idArrs);
        return tags;
    }

    /**
     * 更新标签内容
     */
    @Override
    @Transactional
    public boolean updateTag(Long id, Tag tag) {
        // 确保DB中存在这个Type标签
        Tag tag1 = tagMapper.getTag(id);
        if (tag1 == null) throw new NotFoundException("不存在该标签");

        int result = tagMapper.updateTag(id, tag);
        if (result != 1) return false;
        else return true;
    }

    /**
     * 删除标签
     * 考虑一个问题，如果有blog存在这个标签怎么办
     */
    @Override
    @Transactional
    public boolean deleteTag(Long id) throws BindingException {
        Tag t = new Tag();
        t.setId(id);

        List<Blog> allBlog = blogTagsMapper.findAllBlog(t);
        if (allBlog != null && allBlog.size() > 0) {
            log.warn("此标签已有对应的博客绑定，请修改后再删除" + "allBlog : {} ", allBlog);
            throw new BindingException("有博客绑定中，不能删除");
        }

        blogTagsMapper.deleteRealtionBlogs(t);
        int result = tagMapper.deleteTag(id);
        if (result != 1) return false;
        else return true;
    }
}
