package com.juntao.blogsystem.vo;

import com.juntao.blogsystem.model.Catalog;

public class CatalogVO {
    private String username;
    private Catalog catalog;

    public CatalogVO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
