package com.juntao.blogsystem.service;

import com.juntao.blogsystem.model.Authority;

import java.util.Optional;

public interface AuthorityService {
    /**
     * 根据ID查询Authority
     * @param id
     * @return
     */
    Optional<Authority> getAuthorityById(long id);

    /**
     * 根据权限名查询权限
     * @param name
     * @return
     */
    Authority getAuthorityByName(String name);
}
