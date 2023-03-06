package com.example.e_voter.service;

import com.example.e_voter.data.dto.request.RegisterCandidateRequest;
import com.example.e_voter.data.dto.request.RegisterVoterRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.Candidate;
import com.example.e_voter.data.models.Voter;
import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;

public interface CandidateService {

    RegisterResponse register(RegisterCandidateRequest registerRequest);
    List<Candidate> getAllCandidates();
    Candidate getCandidateById(Long candidateId);
    void updateCandidate(Long candidateId, String name, String party);
    Candidate updateCandidate(Long candidateId, JsonPatch updatePayload);
    void deleteCandidate(Long candidateId);
}
