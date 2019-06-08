package com.juntao.blogsystem.dao;

import com.juntao.blogsystem.model.Authority;
import com.juntao.blogsystem.model.User;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.naming.ldap.PagedResultsControl;
import java.util.Collection;
import java.util.List;



@Repository
public interface UserDao extends JpaRepository<User,Long> {
    //根据用户名分页查询用户列表
    public Page<User> findByUsernameLike(String username, Pageable pageable);

    //根据用户名查找用户
    public User findByUsername(String username);


}
