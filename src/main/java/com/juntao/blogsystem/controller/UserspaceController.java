package com.juntao.blogsystem.controller;

import com.juntao.blogsystem.model.*;
import com.juntao.blogsystem.service.BlogService;
import com.juntao.blogsystem.service.CatalogService;
import com.juntao.blogsystem.service.UserService;
import com.juntao.blogsystem.util.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Controller()
public class UserspaceController {
    @Autowired
    private  UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CatalogService catalogService;

    @Resource
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    /**（中文乱码问题）
     * 用户的主页
     * @param username
     * @param model
     * @return
     */
    @GetMapping(value = "/u/{username}")
    public String userSpace(@PathVariable("username")String username,Model model) throws UnsupportedEncodingException {
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return  "redirect:/u/"+username+"/blogs";
    }
    /**
     * 获取用户主页
     * @param username
     * @param order
     * @param catalogId
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping(value = "/u/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username")String username,
                                   @RequestParam(value = "order",required = false,defaultValue = "new")String order,
                                   @RequestParam(value = "catalog",required = false)Long catalogId,
                                   @RequestParam(value = "keyword",required = false,defaultValue ="")String keyword,
                                   @RequestParam(value="async",required=false)boolean async,
                                   @RequestParam(value="pageIndex",required =false,defaultValue = "0")int pageIndex,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,Model model){
        User user =(User)userDetailsService.loadUserByUsername(username);
        Page<Blog> page =null;
        if (catalogId!=null && catalogId>0) { //分类查询
            Catalog catalog = catalogService.getCatalogById(catalogId).get();
            Pageable pageable =  PageRequest.of(pageIndex,pageSize);
            page = blogService.listBlogByCatalog(catalog,pageable);
            order="";
        }else if(order.equals("hot")){ //最热查询
            Sort sort =new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
            Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
            page = blogService.listBlogsByTitleVoteAndSort(user,keyword,pageable);
        }else if(order.equals("new")) { //最新查询
            Pageable pageable =PageRequest.of(pageIndex,pageSize);
            page = blogService.listBlogsByTitleVote(user,keyword,pageable);
    }
        List<Blog> list =page.getContent(); //当前所在页面数据列表
        model.addAttribute("user",user);
        model.addAttribute("order",order);
        model.addAttribute("catalogId",catalogId);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);
        return (async==true?"/userspace/u ::#mainContainerRepleace":"/userspace/u");
    }
    /**
    * 获取博客展示界面
     * @param username
     * @param id
     * @param model
     * @return
    */
    @GetMapping(value = "/u/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username")String username,@PathVariable("id")Long id,Model model) {
        User principal =null;
        Optional<Blog> blog =blogService.getBlogById(id);
        //每次读取，简单地可以认为阅读量增加1次
        blogService.readingIncrease(id);
        //判断操作用户是否为博客的所有者
        boolean isBlogOwner =false;
        if(SecurityContextHolder.getContext().getAuthentication()!=null &&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            principal =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal!=null && username.equals(principal.getUsername())) {
                 isBlogOwner=true;
            }
        }
        //判断操作用户的点赞情况
        List<Vote> votes = blog.get().getVotes();
        Vote currentVote =null;  //当前用户的点赞情况
        if (principal!=null) {
            for (Vote vote :votes) {
                if(vote.getUser().getUsername().equals(principal.getUsername())) {
                    currentVote =vote;
                    break;
                }
            }
        }
        model.addAttribute("currentVote",currentVote);
        model.addAttribute("isBlogOwner",isBlogOwner);
        model.addAttribute("blogModel",blog.get());
        return "/userspace/blog";
    }

    /**
     * 获取新增博客界面
     * @param model
     * @return
     */
    @GetMapping("/u/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username")String username,Model model){
        //获取用户分类列表
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Catalog> catalogs = catalogService.ListCatalogs(user);
        model.addAttribute("catalogs",catalogs);
        model.addAttribute("blog",new Blog(null,null,null));
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 获取编辑博客的界面
     * @param
     * @return
     */
    @GetMapping("/u/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username")String username,@PathVariable("id")Long id,Model model){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Catalog> catalogs = catalogService.ListCatalogs(user);
        model.addAttribute("catalogs",catalogs);
        model.addAttribute("blog",blogService.getBlogById(id).get());
        model.addAttribute("fileServerUrl",fileServerUrl);
        //文件服务器的地址返回给客户端
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/u/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username")String username,@RequestBody Blog blog) {
        //对Catalog进行空处理
        if(blog.getCatalog().getId()==null) {
            return ResponseEntity.ok().body(new Response(false,"未选择分类"));
        }
        try {
            //判断是修改还是新增
            if (blog.getId() != null) {
                Optional<Blog> optionalBlog = blogService.getBlogById(blog.getId());
                if (optionalBlog.isPresent()) {
                    Blog originalBlog = optionalBlog.get();
                    originalBlog.setTitle(blog.getTitle());
                    originalBlog.setContent(blog.getContent());
                    originalBlog.setSummary(blog.getSummary());
                    originalBlog.setTags(blog.getTags());
                    if(blog.getCatalog().getId()!=0) {
                        originalBlog.setCatalog(blog.getCatalog());
                    }else {
                        originalBlog.setCatalog(null);
                    }
                    blogService.saveBlog(originalBlog);
                }
            }else {
                User user = (User)userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                if(blog.getCatalog().getId()==0) {
                    blog.setCatalog(null);
                }
                blogService.saveBlog(blog);
            }
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch(Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirectUrl ="/u/"+username+"/blogs/"+blog.getId();
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }

    /**
     * 删除博客
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/u/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response>deleteBlog(@PathVariable("username")String username,@PathVariable("id")Long id) {
        try {
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
       String redirectUrl ="/u/"+username +"/blogs";
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }

    /**
     * 获取个人设置的页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/u/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")  //只有用户自己才有权限调用此方法
    public ModelAndView profile(@PathVariable("username")String username, Model model) {
        User user=(User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl); //文件服务器地址返回给客户端
        return new ModelAndView("/userspace/profile","userModel",model);
    }

    /**
     * 保存个人设置
     * @param username
     * @param  user
     * @return
     */
    @PostMapping("/u/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")  //只有用户自己才有权限调用此方法
    public String saveProfile(@PathVariable("username")String username,User user) {
        User originalUser =userService.getUserById(user.getId()).get();
        originalUser.setEmail(user.getEmail());
        //判断密码是否发生改变
        String rawPassword =originalUser.getPassword();
        PasswordEncoder encoder =new BCryptPasswordEncoder();
        String encodePassword =encoder.encode(user.getPassword()); //解密
        if (!encoder.matches(rawPassword,encodePassword)) {
            originalUser.setPassword(user.getPassword());
        }
        userService.saveOrUpdate(originalUser);
        return "redirect:/u/"+username+"/profile";
    }

    /**
     * 获取编辑头像的界面
     * @param username
     * @param model
     * @returm
     */
    @GetMapping("/u/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username,Model model){
        User user =(User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("/userspace/avatar","userModel",model);
    }

    /**
     * 保存头像
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/u/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username,@RequestBody User user) {
        String avatarUrl =user.getAvatar();
        User originalUser =userService.getUserById(user.getId()).get();
        originalUser.setAvatar(avatarUrl);
        userService.saveOrUpdate(originalUser);
        return ResponseEntity.ok().body(new Response(true,"处理成功",avatarUrl));
    }

}
