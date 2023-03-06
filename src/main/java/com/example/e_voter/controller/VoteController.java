package com.example.e_voter.controller;

import com.example.e_voter.data.dto.request.VoteRequest;
import com.example.e_voter.data.models.Ballot;
import com.example.e_voter.data.models.Candidate;
import com.example.e_voter.data.models.Vote;
import com.example.e_voter.data.models.Voter;
import com.example.e_voter.data.repositories.VoterRepository;
import com.example.e_voter.service.BallotService;
import com.example.e_voter.service.CandidateService;
import com.example.e_voter.service.VoteService;
import com.example.e_voter.service.VoterService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v5/votes")
public class VoteController {

    private final VoterService voterService;
    private final CandidateService candidateService;
    private final BallotService ballotService;
    private final VoteService voteService;

    @PostMapping("/cast")
    public ResponseEntity<?> castVote(@RequestBody VoteRequest voteRequest){
        Voter voter = voterService.getVoterById(voteRequest.getVoterId());
        Candidate candidate = candidateService.getCandidateById(voteRequest.getCandidateId());
        Ballot ballot = ballotService.getBallotById(voteRequest.getBallotId());

        Vote vote = new Vote();
        vote.setCandidate(candidate);
        vote.setVoter(voter);
        vote.setBallot(ballot);
        return ResponseEntity.status(HttpStatus.OK).body(vote);
    }

}
