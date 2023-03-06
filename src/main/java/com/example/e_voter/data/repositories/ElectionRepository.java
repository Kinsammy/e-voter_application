package com.example.e_voter.data.repositories;

import com.example.e_voter.data.models.Election;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectionRepository extends JpaRepository<Election, Long> {
    Optional<Election> findByName(String name);
}
