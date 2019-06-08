package com.juntao.blogsystem.service;

import com.juntao.blogsystem.dao.CommentDao;
import com.juntao.blogsystem.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentDao commentDao;

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentDao.findById(id);
    }

    @Override
    public void removeComment(Long id) {
        commentDao.deleteById(id);
    }
}
