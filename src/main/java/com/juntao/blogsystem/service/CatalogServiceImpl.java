package com.juntao.blogsystem.service;

import com.juntao.blogsystem.dao.CatalogDao;
import com.juntao.blogsystem.model.Catalog;
import com.juntao.blogsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogServiceImpl implements CatalogService{
    @Autowired
    private CatalogDao catalogDao;

    @Override
    public Catalog saveCatalog(Catalog catalog) {
        return catalogDao.save(catalog);
    }

    @Override
    public void removeCatalog(Long id) {
        catalogDao.deleteById(id);
    }

    @Override
    public Optional<Catalog> getCatalogById(Long id) {
        return catalogDao.findById(id);
    }

    @Override
    public List<Catalog> ListCatalogs(User user) {
        return  catalogDao.findByUser(user);
    }
}
