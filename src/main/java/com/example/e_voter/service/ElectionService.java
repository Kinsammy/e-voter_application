package com.example.e_voter.service;

import com.example.e_voter.data.dto.request.RegisterElectionRequest;
import com.example.e_voter.data.dto.request.RegisterVoterRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.Election;
import com.example.e_voter.data.models.Voter;
import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

public interface ElectionService {
    RegisterResponse register(RegisterElectionRequest registerRequest);
    List<Election> getAllElections();
    Election getElectionById(Long electionId);
    void updateElection(Long electionId, String name);
    Election updateElection(Long electionId, JsonPatch updatePayload);
    void deleteElection(Long electionId);
}
