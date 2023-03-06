package com.example.e_voter.service.implementations;

import com.example.e_voter.data.dto.request.RegisterBallotRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.*;
import com.example.e_voter.data.repositories.BallotRepo;
import com.example.e_voter.data.repositories.VoteRepository;
import com.example.e_voter.exception.VoterManagementException;
import com.example.e_voter.service.BallotService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class BallotServiceImpl implements BallotService {

    private final BallotRepo ballotRepo;
    private final VoteRepository voteRepository;

    @Override
    public RegisterResponse register(RegisterBallotRequest registerRequest) {
        ModelMapper mapper = new ModelMapper();
        Ballot ballot = mapper.map(registerRequest, Ballot.class);
        ballot.setCreatedAt(LocalDateTime.now().toString());
        ballot.setBallotStatus(BallotStatus.CREATED);
        Ballot savedBallot = ballotRepo.save(ballot);
        RegisterResponse response = getRegisterResponse(savedBallot);
        return response;
    }

    private RegisterResponse getRegisterResponse(Ballot savedBallot) {
        var response = new RegisterResponse();
        response.setId(savedBallot.getId());
        response.setCode(HttpStatus.CREATED.value());
        response.setSuccess(true);
        response.setMessage("Ballot Registration Successful");
        return response;
    }

    @Override
    public List<Ballot> getAllBallots() {
        return ballotRepo.findAll();
    }

    @Override
    public Ballot getBallotById(Long ballotId) {
        return ballotRepo.findById(ballotId).orElseThrow(()->
                new VoterManagementException(
                        String.format("Ballot with id %d not found", ballotId)));
    }

    @Override
    public Ballot updateBallot(Long ballotId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Ballot foundBallot = getBallotById(ballotId);
        JsonNode node = mapper.convertValue(foundBallot, JsonNode.class);
        try{
            JsonNode updatedNode = updatePayload.apply(node);
            var updatedBallot = mapper.convertValue(updatedNode, Ballot.class);
            updatedBallot = ballotRepo.save(updatedBallot);
            return updatedBallot;
        } catch (JsonPatchException exception){
            log.error(exception.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteBallot(Long ballotId) {
//        boolean isExist = ballotRepos.existsById(ballotId);
//        if (isExist){
//            throw new IllegalStateException(
//                    String.format("Ballot with id %d does not exist", ballotId)
//            );
//        }
        ballotRepo.deleteById(ballotId);
    }

    @Override
    public Ballot createBallot(PollingUnit pollingUnit, Election election) {
        Ballot ballot = new Ballot();
        ballot.setPollingUnit(pollingUnit);
        ballot.setElection(election);
        ballot.setBallotStatus(BallotStatus.CREATED);
        return ballotRepo.save(ballot);
    }

    @Override
    public void openBallot(Ballot ballot) {
        if (ballot.getBallotStatus() != BallotStatus.CREATED){
            throw new VoterManagementException("Cannot open ballot in current polling unit.");
        }
        ballot.setBallotStatus(BallotStatus.OPEN);
        ballotRepo.save(ballot);
    }

    @Override
    public void closeBallot(Ballot ballot) {
        if (ballot.getBallotStatus() != BallotStatus.OPEN){
            throw new VoterManagementException("Cannot close ballot in current polling unit");
        }
        ballot.setBallotStatus(BallotStatus.CLOSED);
        ballotRepo.save(ballot);
    }

    @Override
    public Map<Candidate, Long> countVotes(Ballot ballot) {
        if (ballot.getBallotStatus() != BallotStatus.CLOSED){
            throw new VoterManagementException(" Cannot count votes in current polling unit");
        }
        Map<Candidate, Long> voteCounts = new HashMap<>();
        List<Vote> votes = voteRepository.findByBallot(ballot);
        for (Vote vote: votes){
            Candidate candidate = vote.getCandidate();
            Long count = voteCounts.getOrDefault(candidate, 0L);
            voteCounts.put(candidate, count + 1);
        }
        return voteCounts;
    }

    @Override
    public void certifyResults(Ballot ballot) {
        if (ballot.getBallotStatus() != BallotStatus.CLOSED){
            throw new VoterManagementException("Cannot certify results in current polling unit");
        }
        ballot.setBallotStatus(BallotStatus.CERTIFIED);
        ballotRepo.save(ballot);
    }
}
