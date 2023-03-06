package com.example.e_voter.service;

import com.example.e_voter.data.dto.request.RegisterVoterRequest;
import com.example.e_voter.data.dto.request.VoteRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.dto.response.VoteResponse;
import com.example.e_voter.data.models.*;
import com.github.fge.jsonpatch.JsonPatch;
import jdk.dynalink.linker.LinkerServices;

import java.util.List;

public interface VoterService {
    RegisterResponse register(RegisterVoterRequest registerRequest);
    List<Voter> getAllVoters();
    Voter getVoterById(Long voterId);
    void updateVoter(Long voterId, String name, String email);
    Voter updateVoter(Long voterId, JsonPatch updatePayload);
    void deleteVoter(Long voterId);
//    VoteResponse castVote(VoteRequest voteRequest);

}
