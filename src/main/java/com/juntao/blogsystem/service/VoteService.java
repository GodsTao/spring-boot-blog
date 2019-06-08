package com.juntao.blogsystem.service;

import com.juntao.blogsystem.dao.VoteDao;
import com.juntao.blogsystem.model.Vote;

import java.util.Optional;

public interface VoteService {

    /**
     * 根据id获取Vote
     * @param id
     * @return
     */
    Optional<Vote> getVoteById(Long id);

    /**
     * 删除Vote
     * @param id
     * @return
     */
    void removeVote(Long id);
}
