package com.example.e_voter.data.repositories;

import com.example.e_voter.data.models.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoterRepository extends JpaRepository<Voter, Long> {
    Optional<Voter> findVoterByEmail(String email);
}
