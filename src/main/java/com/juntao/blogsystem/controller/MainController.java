package com.juntao.blogsystem.controller;

import com.juntao.blogsystem.model.Authority;
import com.juntao.blogsystem.model.User;
import com.juntao.blogsystem.service.AuthorityService;
import com.juntao.blogsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    private static final Long ROLE_USER_AUTHORITY_ID =2L;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/")
    public String root() {
        return "redirect:/blogs";
    }

    @GetMapping("/index")
    public String toIndex(){
        return "redirect:/blogs";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/toLogin")
    public ModelAndView toLogin() {
        return new ModelAndView("login1");
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError",true);
        model.addAttribute("error","用户名或密码错误");
        return "login";
    }

    @GetMapping("/toRegister")
    public ModelAndView toRegister(){
        return new ModelAndView("register1");
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    /*
    * 注册用户
    * @param user
    * @return
    * */
    @PostMapping("/doRegister")
    public String doRegister(User user){
        List<Authority> authorities=new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID).get());
        user.setAuthorities(authorities);
        userService.saveOrUpdate(user);
        return "redirect:/login";
    }

}
