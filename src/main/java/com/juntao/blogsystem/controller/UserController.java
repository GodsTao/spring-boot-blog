package com.juntao.blogsystem.controller;


import com.juntao.blogsystem.model.Authority;
import com.juntao.blogsystem.model.Response;
import com.juntao.blogsystem.model.User;
import com.juntao.blogsystem.service.AuthorityService;
import com.juntao.blogsystem.service.UserService;
import com.juntao.blogsystem.util.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;

    /*
    * 查询所有用户
    * @param async 权限
    * @param pageIndex
    * @param pageSize
    * @param
    */
    @GetMapping
    public ModelAndView list(@RequestParam(value = "async",required = false)boolean async,
                             @RequestParam(value ="pageIndex",required = false,defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                             @RequestParam(value = "username",required = false,defaultValue ="")String username, Model model) {
        Pageable pageable =PageRequest.of(pageIndex,pageSize);
        Page<User> page =userService.listUsersByNameLike(username,pageable);

        List<User> list =page.getContent(); //当前所在页面数据列表
        model.addAttribute("page",page);
        model.addAttribute("userList",list);
        return new ModelAndView(async==true? "users/list ::#mainContainerRepleace":"users/list","userModel",model);
    }

    /**
     * 获取创建表单页面
     * @param model
     * @return
     */
    @GetMapping("/add")
    public ModelAndView createForm(Model model){
         model.addAttribute("user",new User(null,null,null));
         return new ModelAndView("users/add","userModel",model);
    }

    /**保存或修改用户
     * @param user
     * @param authorityId
     * @return
     */
    @PostMapping
    //注意一定不能用long接受数据，要用基础类型Long
    public ResponseEntity<Response> saveOrUpdateUser(User user,Long authorityId ) {
        List<Authority> authorities =new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(authorityId).get());
        user.setAuthorities(authorities);
        try{
            userService.saveOrUpdate(user);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功",user));
    }

    /**
     * 删除用户
     * @param id
     * @param model
     * @return
     * */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id,Model model) {
        try{
            userService.removeUser(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }
    /**
     * 获取修改用户的界面
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id")Long id,Model model) {
       Optional<User> user =userService.getUserById(id);  //Optional 解决空指针异常
        model.addAttribute("user",user.get());
        return new ModelAndView("users/edit","userModel",model);
    }
}
