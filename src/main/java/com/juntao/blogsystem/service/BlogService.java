package com.juntao.blogsystem.service;

import com.juntao.blogsystem.model.Blog;
import com.juntao.blogsystem.model.Catalog;
import com.juntao.blogsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BlogService {
    /**
     * 保存Blog
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 删除Blog
     * @param id
     * @return
     */
    void removeBlog(Long id);

    /**
     * 根据id获取Blog
     * @param id
     * @return
     */
    Optional<Blog> getBlogById(Long id);

    /**
     * 根据用户进行博客名称分页模糊查询(最新)
     * @param user
     * @return
     */
    Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable);

    /**
     * 根据用户进行博客名称分页模糊查询（最热）
     * @param user
     * @return
     */
    Page<Blog>listBlogsByTitleVoteAndSort(User user,String title,Pageable pageable);

    /**
     * 阅读量增加
     * @param id
     */
    void readingIncrease(Long id);

    /**
     * 增添评论
     * @param blogId
     * @param commentContent
     * @return
     */
    Blog createComment(Long blogId,String commentContent);

    /**
     * 删除评论
     * @param blogId
     * @param commentId
     * @return
     */
    void removeComment(Long blogId,Long commentId);

    /**
     * 点赞
     * @param blogId
     * @return
     */
    Blog createVote(Long blogId);


    /**
     * 取消点赞
     * @param blogId
     * @param voteId
     * @return
     */
    void removeVote(Long blogId,Long voteId);

    /**
     * 根据分类获取博客
     * @param catalog
     * @param pageable
     * @return
     */
    Page<Blog> listBlogByCatalog(Catalog catalog,Pageable pageable);
}
