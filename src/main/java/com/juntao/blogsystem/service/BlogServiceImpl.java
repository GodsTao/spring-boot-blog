package com.juntao.blogsystem.service;

import com.juntao.blogsystem.dao.BlogDao;
import com.juntao.blogsystem.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private  EsBlogService esBlogService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew =(blog.getId()==null);
        EsBlog esBlog = null;
        Blog returnBlog =blogDao.save(blog);
        if (isNew) {
            esBlog = new EsBlog(returnBlog);
        }else {
            esBlog =esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }
        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogDao.deleteById(id);
        EsBlog esBlog =esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
    }

    @Override
    public Optional<Blog> getBlogById(Long id) {
        return blogDao.findById(id);
    }

    @Override
    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
        //模糊查询
        title ="%"+title +"%";
        String tags =title;
        Page<Blog> blogs =blogDao.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user,tags,user,pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
        //模糊查询
        title ="%"+title+"%";
        String tags =title;
        Page<Blog> blogs =blogDao.findByUserAndTitleLike(user,title,pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Optional<Blog> blog =blogDao.findById(id);
        Blog blogNew =null;
        if(blog.isPresent()){
            blogNew =blog.get();
            blogNew.setReadSize(blogNew.getReadSize()+1); //在原有的阅读量基础上递增1
            this.saveBlog(blogNew);
        }
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Optional<Blog> optionalBlog = blogDao.findById(blogId);
        Blog originalBlog = null;
        if (optionalBlog.isPresent()) {
            originalBlog =optionalBlog.get();
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Comment comment = new Comment(user,commentContent);
            originalBlog.addComment(comment);
        }
        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Optional<Blog> optionalBlog = blogDao.findById(blogId);
        if(optionalBlog.isPresent()) {
            Blog originalBlog = optionalBlog.get();
            originalBlog.removeComment(commentId);
            this.saveBlog(originalBlog);
        }
    }

    @Override
    public Blog createVote(Long blogId) {
        Optional<Blog> optionalBlog = blogDao.findById(blogId);
        Blog originalBlog =null;
        if (optionalBlog.isPresent()) {
            originalBlog = optionalBlog.get();

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Vote vote = new Vote(user);
            boolean isExist = originalBlog.addVote(vote);
            if (isExist) {
                throw new IllegalArgumentException("该用户已经点过赞了");
            }
        }
        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Optional<Blog> optionalBlog = blogDao.findById(blogId);
        Blog originalBlog = null;
        if(optionalBlog.isPresent()) {
            originalBlog = optionalBlog.get();
            originalBlog.removeVote(voteId);
            this.saveBlog(originalBlog);
        }
    }

    @Override
    public Page<Blog> listBlogByCatalog(Catalog catalog, Pageable pageable) {
        return blogDao.findByCatalog(catalog,pageable);
    }
}
