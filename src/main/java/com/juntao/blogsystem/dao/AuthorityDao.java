package com.juntao.blogsystem.dao;

import com.juntao.blogsystem.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityDao extends JpaRepository<Authority,Long> {
    //通过权限名获得权限
   public Authority findAuthoritiesByName(String name);
}
