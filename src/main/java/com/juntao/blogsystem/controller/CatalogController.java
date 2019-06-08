package com.juntao.blogsystem.controller;

import com.juntao.blogsystem.model.Catalog;
import com.juntao.blogsystem.model.Response;
import com.juntao.blogsystem.model.User;
import com.juntao.blogsystem.service.CatalogService;
import com.juntao.blogsystem.util.ConstraintViolationExceptionHandler;
import com.juntao.blogsystem.vo.CatalogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/catalogs")
public class CatalogController  {

    @Autowired
    private CatalogService catalogService;

    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 获取分类列表
     * @param username
     * @param model
     * @return
     */
    @GetMapping
    public String listCatalogs(@RequestParam(value = "username",required = true)String username, Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.ListCatalogs(user);

        //判断是否为分类的所有者
        boolean isOwner =false;
        if (SecurityContextHolder.getContext().getAuthentication()!=null
                &&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal!=null &&principal.getUsername().equals(user.getUsername())){
                isOwner=true;
            }
        }
        model.addAttribute("isCatalogsOwner",isOwner);
        model.addAttribute("catalogs",catalogs);
        return "/userspace/u :: #catalogRepleace";
    }

    /**
     * 创建分类
     * @param catalogVO
     * @return
     */
    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogVO.username)")  //指定用户才能操作
    public ResponseEntity<Response> createCatelog(@RequestBody CatalogVO catalogVO) {
        String username = catalogVO.getUsername();
        Catalog catalog = catalogVO.getCatalog();

        User user = (User)userDetailsService.loadUserByUsername(username);
        try {
            catalog.setUser(user);
            catalogService.saveCatalog(catalog);
        }catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }

    /**
     * 删除分类
     * @param name
     * @param id
     * @return
     */
    @DeleteMapping
    @PreAuthorize("authentication.name.equals(#catalogVO.username)")
    public ResponseEntity<Response> deleteCatalog(String name,@PathVariable("id") Long id){
        try {
            catalogService.removeCatalog(id);
        }catch(ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
        }catch(Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功",null));
    }

    /**
     * 获取分类编辑界面
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String getCatalogEdit(Model model) {
        Catalog catalog = new Catalog(null,null);
        model.addAttribute("catalog",catalog);
        return "/userspace/catalogedit";
    }

    /**
     * 根据id获取编辑界面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") Long id,Model model) {
        Optional<Catalog> optionalCatalog =catalogService.getCatalogById(id);
        Catalog catalog =null;
        if(optionalCatalog.isPresent()) {
            catalog =optionalCatalog.get();
        }
        model.addAttribute("catalog",catalog);
        return "/userspace/catalogedit";
    }

}
