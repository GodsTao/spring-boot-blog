package com.juntao.blogsystem.dao;

import com.juntao.blogsystem.model.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsDao extends ElasticsearchRepository<EsBlog,String> {
    /**
     * 模糊查询
     * @param title
     * @param summary
     * @param content
     * @param tags
     * @param pageable
     * @return
     */
    Page<EsBlog> findByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title,
                 String summary,String content,String tags,Pageable pageable);

    EsBlog findByBlogId(Long blogId);
}
