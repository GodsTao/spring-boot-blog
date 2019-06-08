package com.juntao.blogsystem.controller;

import com.juntao.blogsystem.model.Blog;
import com.juntao.blogsystem.model.Comment;
import com.juntao.blogsystem.model.Response;
import com.juntao.blogsystem.model.User;
import com.juntao.blogsystem.service.BlogService;
import com.juntao.blogsystem.service.CommentService;
import com.juntao.blogsystem.util.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/comments")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    /**
     * 获取评论
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping
    public String listComments(@RequestParam(value = "blogId")Long blogId, Model model) {
        Optional<Blog> optionalBlog = blogService.getBlogById(blogId);
        List<Comment> comments =null;
        if(optionalBlog.isPresent()) {
            comments =optionalBlog.get().getComments();
        }
        //判断是否为评论的所有者
        String commentOwner =null;
        if(SecurityContextHolder.getContext().getAuthentication()!=null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal!=null) {
                commentOwner =principal.getUsername();
            }
        }
        model.addAttribute("commentOwner",commentOwner);
        model.addAttribute("comments",comments);
        return "/userspace/blog :: #mainContainerRepleace";
    }

    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createComment(Long blogId,String commentContent) {
        try{
            blogService.createComment(blogId,commentContent);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功",null));
    }

    /**
     * 删除评论
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> deleteComment( Long blogId, @PathVariable("id") Long id) {
        boolean isOwner = false;
        Optional<Comment> optionalComment = commentService.getCommentById(id);
        User user =null;
        if (optionalComment.isPresent()) {
            user =optionalComment.get().getUser();
        }else {
            return ResponseEntity.ok().body(new Response(false,"不存在该评论"));
        }
        //判断是否为评论的所有者
        if (SecurityContextHolder.getContext().getAuthentication()!=null
                &&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal !=null &&user.getUsername().equals(principal.getUsername())) {
                isOwner =true;
            }
        }
        if (!isOwner) {
            return ResponseEntity.ok().body(new Response(false,"没有操作权限"));
        }
        try{
            blogService.removeComment(blogId,id);
            commentService.removeComment(id);
        }catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功",null));
    }
}
