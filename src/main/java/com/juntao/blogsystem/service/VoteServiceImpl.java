package com.juntao.blogsystem.service;

import com.juntao.blogsystem.dao.VoteDao;
import com.juntao.blogsystem.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteDao voteDao;
    @Override
    public Optional<Vote> getVoteById(Long id) {
        return voteDao.findById(id);
    }

    @Override
    public void removeVote(Long id) {
        voteDao.deleteById(id);
    }
}
