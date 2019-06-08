package com.juntao.blogsystem.dao;

import com.juntao.blogsystem.model.Catalog;
import com.juntao.blogsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogDao extends JpaRepository<Catalog,Long> {

    /**
     * 根据用户查询
     * @param user
     * @return
     */
    List<Catalog> findByUser(User user);

    /**
     *根据用户和分类名查询
     * @param user
     * @param naem
     * @return
     */
    List<Catalog> findByUserAndAndName(User user,String naem);
}
