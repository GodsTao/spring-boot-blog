package com.juntao.blogsystem.model;

import org.springframework.security.core.GrantedAuthority;

import javax.annotation.Generated;
import javax.persistence.*;

@Entity
@Table(name = "blog_authority")
public class Authority implements GrantedAuthority {
    private  static final long serialVersionUID =1L; //序列化ID，用于验证版本的一致性

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //用户的唯一标识

    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
