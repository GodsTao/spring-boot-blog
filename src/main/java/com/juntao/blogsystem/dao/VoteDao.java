package com.juntao.blogsystem.dao;

import com.juntao.blogsystem.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteDao extends JpaRepository<Vote,Long> {
}
