package com.juntao.blogsystem.service;

import com.juntao.blogsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * 新增，编辑保存用户
     * @param user
     * @return
     **/
    User saveOrUpdate(User user);


    /**
     * 删除用户
     * @param id
     **/
    void removeUser(Long id);

    /**
     * 根据用户名获取用户
     * @param usernames
     * @return
     **/
    List<User> listUsersByUsernames(List<String> usernames);

    /**
     * 根据用户名获取用户
     * @param id
     * @return
     **/
    Optional<User> getUserById(long id);

    /**
     * 根据用户名进行分页模糊查询
     * @param username
     * @param pageable
     * @return
     **/
    Page<User> listUsersByNameLike(String username, Pageable pageable);



}
