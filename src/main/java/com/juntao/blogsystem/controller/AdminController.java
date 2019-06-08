package com.juntao.blogsystem.controller;

import com.juntao.blogsystem.model.Menu;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {
    @GetMapping
    public ModelAndView listUsers(Model model) {
        List<Menu> list =new ArrayList<>();
        list.add(new Menu("用户管理","/users"));
        model.addAttribute("list",list);
        return new ModelAndView("/admins/index","model",model);
    }
}
