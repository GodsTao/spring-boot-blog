package com.juntao.blogsystem.service;

import com.juntao.blogsystem.model.EsBlog;
import com.juntao.blogsystem.model.User;
import com.juntao.blogsystem.vo.TagVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EsBlogService {
    /**
     * 删除EsBlog
     * @param id
     */
    void removeEsBlog(String id);

    /**
     * 更新EsBlog
     * @param esBlog
     * @return
     */
    EsBlog updateEsBlog(EsBlog esBlog);

    /**
     * 根据Blog的id获取EsBlog
     * @param blogId
     * @return
     */
    EsBlog getEsBlogByBlogId(Long blogId);

    /**
     * 最新博客列表,分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listNewestEsBlog(String keyword, Pageable pageable);

    /**
     * 最热博客列表，分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listHotestEsBlog(String keyword,Pageable pageable);

    /**
     * 博客列表，分页
     * @param pageable
     * @return
     */
    Page<EsBlog>listEsBlogs(Pageable pageable);

    /**
     * 最新前5
     * @return
     */
    List<EsBlog>listTop5NewestEsBlogs();

    /**
     * 最热前5
     * @return
     */
    List<EsBlog>listTop5HotestEsBlogs();

    /**
     * 最热前30标签
     * @return
     */
    List<TagVO>listTop30Tags();

    /**
     * 最热前12用户
     * @return
     */
    List<User>listTop12Users();

}
