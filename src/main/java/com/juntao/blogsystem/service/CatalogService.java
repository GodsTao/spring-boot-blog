package com.juntao.blogsystem.service;

import com.juntao.blogsystem.model.Catalog;
import com.juntao.blogsystem.model.User;

import java.util.List;
import java.util.Optional;

public interface CatalogService {

    /**
     * 保存Catalog
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除Catalog
     * @param id
     */
    void removeCatalog(Long id);

    /**
     * 根据id获取Catalog
     * @param id
     * @return
     */
    Optional<Catalog> getCatalogById(Long id);

    /**
     * 获取分类列表
     * @param user
     * @return
     */
    List<Catalog> ListCatalogs(User user);
}
