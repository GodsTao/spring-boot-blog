package com.juntao.blogsystem.service;

import com.juntao.blogsystem.dao.AuthorityDao;
import com.juntao.blogsystem.model.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorityIServiceImpl implements AuthorityService{
    @Autowired
    private AuthorityDao authorityDao;

    @Override
    public Optional<Authority> getAuthorityById(long id) {
        return authorityDao.findById(id);
    }

    @Override
    public Authority getAuthorityByName(String name) {
        return authorityDao.findAuthoritiesByName(name);
    }
}
