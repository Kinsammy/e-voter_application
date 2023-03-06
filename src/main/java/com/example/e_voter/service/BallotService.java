package com.example.e_voter.service;

import com.example.e_voter.data.dto.request.RegisterBallotRequest;
import com.example.e_voter.data.dto.request.RegisterCandidateRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.Ballot;
import com.example.e_voter.data.models.Candidate;
import com.example.e_voter.data.models.Election;
import com.example.e_voter.data.models.PollingUnit;
import com.github.fge.jsonpatch.JsonPatch;

import java.util.List;
import java.util.Map;

public interface BallotService {
    RegisterResponse register(RegisterBallotRequest registerRequest);
    List<Ballot> getAllBallots();
    Ballot getBallotById(Long ballotId);
//    void updateCandidate(Long ballotId, String name, String party);
    Ballot updateBallot(Long ballotId, JsonPatch updatePayload);
    void deleteBallot(Long ballotId);
    Ballot createBallot(PollingUnit pollingUnit, Election election);
    void openBallot(Ballot ballot);
    void closeBallot(Ballot ballot);
    Map<Candidate, Long> countVotes(Ballot ballot);
    void certifyResults(Ballot ballot);
}
