package com.example.e_voter.data.repositories;

import com.example.e_voter.data.models.Ballot;
import com.example.e_voter.data.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByBallot(Ballot ballot);
}
