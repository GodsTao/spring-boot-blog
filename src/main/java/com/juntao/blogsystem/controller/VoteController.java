package com.juntao.blogsystem.controller;

import com.juntao.blogsystem.model.Response;
import com.juntao.blogsystem.model.User;
import com.juntao.blogsystem.model.Vote;
import com.juntao.blogsystem.service.BlogService;
import com.juntao.blogsystem.service.VoteService;
import com.juntao.blogsystem.util.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@Controller
@RequestMapping(value = "/votes")
public class VoteController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private VoteService voteService;

    /**
     * 发表点赞
     * @param blogId
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createVote(Long blogId) {
        try{
            blogService.createVote(blogId);
        }catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
      return ResponseEntity.ok().body(new Response(true,"点赞成功",null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> deleteVote(@PathVariable("id")Long id,Long blogId) {
        boolean isOwner =false;
        Optional<Vote> optionalVote = voteService.getVoteById(id);
        User user = null;
        if(optionalVote.isPresent()) {
            user = optionalVote.get().getUser();
        }else {
            return ResponseEntity.ok().body(new Response(false,"您还没有点赞"));
        }
        //判断操作用户是否为点赞的所有者
        if (SecurityContextHolder.getContext().getAuthentication()!=null
                &&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal !=null &&user.getUsername().equals(principal.getUsername())) {
                isOwner =true;
            }
        }
        if(!isOwner) {
            return ResponseEntity.ok().body(new Response(false,"没有操作权限"));
        }
        try {
            blogService.removeVote(blogId,id);
            voteService.removeVote(id);
        }catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"取消点赞成功",null));
    }
}
