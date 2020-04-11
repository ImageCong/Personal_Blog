package com.example.demo;

import com.example.demo.entity.Blog;
import com.example.demo.entity.Tag;
import com.example.demo.entity.Type;
import com.example.demo.entity.User;
import com.example.demo.mapper.BlogMapper;
import com.example.demo.mapper.BlogTagsMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.BlogService;
import com.example.demo.service.TagService;
import com.example.demo.service.TypeService;
import com.example.demo.web.admin.TypeController;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SpringBootTest
@MapperScan(basePackages = {"com.example.demo.mapper"})
class DemoApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    TagService tagService;
    @Autowired
    BlogTagsMapper blogTagsMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testLogin() {
        String username = "admin";
        String password = "admin";

        User user = userMapper.checkUser(username, password);
        System.out.println(user);
    }

    @Test
    void testType() {
//        System.out.println(typeService.getType(1l));

//        List<Type> list = typeService.listType().getList();
//
//        for (Type t : list){
//            System.out.println(t);
//            System.out.println(t.getBlogs());
//            System.out.println();
//        }


        List<Type> list = typeService.listType().getList();
        for (Type t : list){
            t.setBlogs(blogMapper.getBlogByTypeId(6L));
        }

        System.out.println(list);

//        typeController.list(new ExtendedModelMap(), 1, 10);

    }

    @Test
    void testTag() {
//        String s ="1234";
//        List<Tag> tags = tagService.listTagWithCondition(s);
//        for (Tag t : tags){
//            System.out.println(t);
//        }

        List<Tag> list = tagService.listTag().getList();
        System.out.println(list);
    }

    @Test
    void testBlog(){
//        System.out.println(blogService.getBlog(2l));
//        Blog blog = new Blog();
//        blog.setTitle("newTitle");
//        blog.setContent("lllllllllllllllllllllllllllllllllllllllllll");
//        blog.setFirstPicture("process=image/format,png");
//        blogService.updateBlog(2l, blog);
//        blogService.saveBlog(blog);

//        List<Blog> list = blogService.listBlog(null, null, false).getList();
//
//        for (Blog b : list){
//            System.out.println(b);
//            System.out.println();
//        }

        Map<String, List<Blog>> stringListMap = blogService.archivesBlogs();

        Iterator it = stringListMap.keySet().iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }

//        Type type = typeService.getType(1l);
//        PageInfo<Blog> pageInfo = blogService.listBlog(null, type, false);
//        System.out.println(pageInfo);
    }

    @Test
    void testBlogTags(){
        Tag tag = new Tag();
        tag.setId(1L);
        List<Blog> allBlog = blogTagsMapper.findAllBlog(tag);
        System.out.println(allBlog);

        Blog blog = new Blog();
        blog.setId(1L);
        List<Tag> allTags = blogTagsMapper.findAllTags(blog);
        System.out.println(allTags);
    }


}


