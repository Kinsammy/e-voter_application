package com.example.e_voter.service.implementations;

import com.example.e_voter.data.dto.request.VoteRequest;
import com.example.e_voter.data.dto.response.VoteResponse;
import com.example.e_voter.data.models.*;
import com.example.e_voter.data.repositories.*;
import com.example.e_voter.exception.VoterManagementException;
import com.example.e_voter.service.VoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class VoteServiceImpl implements VoteService {
    private final VoterRepository voterRepository;
    private final CandidateRepository candidateRepository;
    private final BallotRepo ballotRepo;
    private final PollingUnitRepository pollingUnitRepository;
    private final VoteRepository voteRepository;

    @Override
    public VoteResponse castVote(VoteRequest voteRequest) {
        Voter voter = voterRepository.findById(voteRequest.getVoterId())
                .orElseThrow(() ->
                        new VoterManagementException("Voter not found"));

        if (voter.getVoterStatus() == VoterStatus.REGISTERED){
            throw new VoterManagementException(
                    String.format("%s has already voted", voter.getName()));
        }
        Candidate candidate = candidateRepository.findById(voteRequest.getCandidateId())
                .orElseThrow(()-> new VoterManagementException("Candidate not found"));
        Ballot ballot = ballotRepo.findById(voteRequest.getBallotId())
                .orElseThrow(()->
                        new VoterManagementException("Ballot not found"));
        PollingUnit pollingUnit = pollingUnitRepository.findById(voteRequest.getPollingUnitId())
                .orElseThrow(()-> new VoterManagementException("Polling unit not found"));
        Vote savedVote = getVote(voter, candidate, ballot, pollingUnit);
        VoteResponse voteResponse = getVoteResponse(savedVote);
        return voteResponse;
    }

    private Vote getVote(Voter voter, Candidate candidate, Ballot ballot, PollingUnit pollingUnit) {
        Vote vote = new Vote();
        vote.setCandidate(candidate);
        vote.setVoter(voter);
        vote.setPollingUnit(pollingUnit);
        vote.setBallot(ballot);
        Vote savedVoted = voteRepository.save(vote);
        vote.setCastedVoteOn(LocalDateTime.now().toString());
        voter.setVoterStatus(VoterStatus.VOTED);
        voterRepository.save(voter);
        pollingUnitRepository.save(pollingUnit);
        ballotRepo.save(ballot);
        return savedVoted;
    }

    private VoteResponse getVoteResponse(Vote savedVoted) {
        VoteResponse voteResponse = new VoteResponse();
        voteResponse.setVoter(savedVoted.getVoter());
        voteResponse.setCandidate(savedVoted.getCandidate());
        voteResponse.setPollingUnit(savedVoted.getPollingUnit());
        voteResponse.setBallot(savedVoted.getBallot());
        voteResponse.setVoteStatus(VoteStatus.CAST);
        return voteResponse;
    }
}
