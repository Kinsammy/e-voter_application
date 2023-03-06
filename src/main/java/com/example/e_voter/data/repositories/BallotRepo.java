package com.example.e_voter.data.repositories;

import com.example.e_voter.data.models.Ballot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BallotRepo extends JpaRepository<Ballot, Long> {
}
