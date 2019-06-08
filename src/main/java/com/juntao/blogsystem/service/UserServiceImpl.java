package com.juntao.blogsystem.service;

import com.juntao.blogsystem.dao.AuthorityDao;
import com.juntao.blogsystem.dao.UserDao;
import com.juntao.blogsystem.model.Authority;
import com.juntao.blogsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService , UserDetailsService {
    @Autowired
    private AuthorityDao authorityDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public User saveOrUpdate(User user) {
        Pattern BCRYPT_PATTERN = Pattern
                .compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
        if(!BCRYPT_PATTERN.matcher(user.getPassword()).matches()){  //如果不像是BCRYT编码就加密
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 加密
            String encodedPassword = passwordEncoder.encode(user.getPassword().trim());
            user.setPassword(encodedPassword);
        }

        return userDao.saveAndFlush(user);
    }


    @Transactional
    @Override
    public void removeUser(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public List<User> listUsersByUsernames(List<String> usernames) {
         List<User> list = new ArrayList<>();
         for (String name : usernames){
            list.add(userDao.findByUsername(name));
         }
         return list;
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userDao.findById(id);
    }

    @Override
    public Page<User> listUsersByNameLike(String username, Pageable pageable) {
        username ="%"+username+"%";
        Page<User> users =userDao.findByUsernameLike(username,pageable);
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByUsername(username);
    }
}
