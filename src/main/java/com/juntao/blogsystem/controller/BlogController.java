package com.juntao.blogsystem.controller;

import com.juntao.blogsystem.model.EsBlog;
import com.juntao.blogsystem.model.User;
import com.juntao.blogsystem.service.EsBlogService;
import com.juntao.blogsystem.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    public EsBlogService esBlogService;

    @GetMapping
    public ModelAndView listEsblogs(@RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                    @RequestParam(value="keyword",required = false,defaultValue = "") String keyword,
                                    @RequestParam(value = "async",required = false) boolean async,
                                    @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                    @RequestParam(value = "pageSize",required = false,defaultValue = "5") int pageSize,
                                    Model model) {
        Page<EsBlog> page = null;
        List<EsBlog> list = null;

        boolean isEmpty = true; //系统初始化时，没有博客数据
        try{
            if (order.equals("hot")) {  //最热查询
                Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
                Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
                page =esBlogService.listHotestEsBlog(keyword,pageable);
            }else if (order.equals("new")) { //最新查询
                Sort sort =new Sort(Sort.Direction.DESC,"createTime");
                Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
                page=esBlogService.listNewestEsBlog(keyword,pageable);
            }
            isEmpty =false;
        }catch (Exception e){
            Pageable pageable =PageRequest.of(pageIndex,pageSize);
            page =esBlogService.listEsBlogs(pageable);
        }
        list = page.getContent(); //当前所在页面的数据列表

        model.addAttribute("order",order);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);

        //首次访问页面才加载
        if (!async &&!isEmpty) {
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest",newest);
            List<EsBlog> hotest =esBlogService.listTop5HotestEsBlogs();
            model.addAttribute("hotest",hotest);
            List<TagVO> tags =esBlogService.listTop30Tags();
            model.addAttribute("tags",tags);
            List<User> users = esBlogService.listTop12Users();
            model.addAttribute("users",users);
        }
        return new ModelAndView(async==true?"/index :: #mainContainerRepleace":"/index","model",model);
    }

    @GetMapping("/getBlogs")
    public Map<String,Object> getBlog(@RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                      @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                      @RequestParam(value = "pageSize",required = false,defaultValue = "5") int pageSize,
                                      @RequestParam(value="keyword",required = false,defaultValue = "") String keyword) {
        Page<EsBlog> page = null;
        List<EsBlog> list = null;

        if (order.equals("hot")) {  //最热查询
            Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
            Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
            page =esBlogService.listHotestEsBlog(keyword,pageable);
        }else if (order.equals("new")) { //最新查询
            Sort sort =new Sort(Sort.Direction.DESC,"createTime");
            Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
            page=esBlogService.listNewestEsBlog(keyword,pageable);
        }
        list=page.getContent();
        Map<String,Object> result = new HashMap<>();
        List<Integer> pageArray = new ArrayList<>();
        for (int i=0;i<page.getTotalPages();i++){
            pageArray.add(i);
        }
        result.put("pageArray",pageArray);
        result.put("page",page);
        result.put("list",list);

        return result;
    }
}
