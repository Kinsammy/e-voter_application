package com.example.e_voter.service.implementations;

import com.example.e_voter.data.models.Ballot;
import com.example.e_voter.data.models.Candidate;
import com.example.e_voter.data.models.Voter;
import com.example.e_voter.service.VoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class VoteServiceImplTest {
    @Autowired
    private VoteService voteService;

    @Test
    void castVote() {
        Voter voter = new Voter();
        Candidate candidate = new Candidate();
        Ballot ballot = new Ballot();
        var vote = voteService.castVote(voter, candidate, ballot);
        assertThat(vote).isNotNull();
    }
}