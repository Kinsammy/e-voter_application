package com.example.e_voter.data.repositories;

import com.example.e_voter.data.models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
