package com.juntao.blogsystem.service;

import com.juntao.blogsystem.model.Comment;

import java.util.Optional;

public interface CommentService {
    /**
     * 根据id获取Comment
     * @param  id
     * @return
     */
    Optional<Comment> getCommentById(Long id);

    /**
     * 根据id删除Comment
     * @param id
     */
    void removeComment(Long id);
}
