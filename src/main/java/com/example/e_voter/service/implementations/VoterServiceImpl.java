package com.example.e_voter.service.implementations;

import com.example.e_voter.data.dto.request.RegisterVoterRequest;
import com.example.e_voter.data.dto.request.VoteRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.dto.response.VoteResponse;
import com.example.e_voter.data.models.*;
import com.example.e_voter.data.repositories.*;
import com.example.e_voter.exception.VoterManagementException;
import com.example.e_voter.service.VoterService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class VoterServiceImpl implements VoterService {

    private final VoterRepository voterRepository;
//    private final CandidateRepository candidateRepository;
//    private final BallotRepo ballotRepo;
//    private final PollingUnitRepository pollingUnitRepository;
//    private final VoteRepository voteRepository;
    @Override
    public RegisterResponse register(RegisterVoterRequest registerRequest) {
        ModelMapper mapper = new ModelMapper();
        Voter voter = mapper.map(registerRequest, Voter.class);
//        validateVoterEmail(voter);
        voter.setCreatedAt(LocalDateTime.now().toString());
        Voter savedVoter = voterStatus(voter);
        RegisterResponse registerResponse = getRegisterResponse(savedVoter);
        return registerResponse;
    }

//    private Voter voterDetails(){
//        Voter voter =
//    }

    private Voter voterStatus(Voter voter) {
        Voter savedVoter = new Voter();
        if (savedVoter.equals(voterRepository.save(voter))){
            voter.setVoterStatus(VoterStatus.REGISTERED);
        } else {
            voter.setVoterStatus(VoterStatus.NOT_REGISTERED);
        }
        return savedVoter;
    }

//    private void validateVoterEmail(Voter voter) {
//        Optional<Voter> voterOptional = voterRepository.findVoterByEmail(voter.getEmail());
//        if (voterOptional.isPresent()){
//            throw new IllegalStateException("Email taken");
//        }
//    }

    private RegisterResponse getRegisterResponse(Voter savedVoter) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(savedVoter.getId());
        registerResponse.setCode(HttpStatus.CREATED.value());
        registerResponse.setSuccess(true);
        registerResponse.setMessage("Voter Registration Successful");
        return registerResponse;
    }

    @Override
    public List<Voter> getAllVoters() {
        return voterRepository.findAll();
    }

    @Override
    public Voter getVoterById(Long voterId) {
        return voterRepository.findById(voterId).orElseThrow(()->
                new VoterManagementException(
                        String.format("Voter with id %d not found", voterId)));
    }

    @Override
    public void updateVoter(Long voterId, String name, String email) {
        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new IllegalStateException(
                        "Voter with id " + voterId + " does not exist"
                ));
        if (name != null &&
                name.length() > 0 &&
                voter.getName().equals(name)){
            voter.setName(name);
        }

        if (email != null &&
        email.length() > 0 &&
        voter.getEmail().equals(email)){
            Optional<Voter> voterOptional = voterRepository.findVoterByEmail(email);
            if (voterOptional.isPresent()){
                throw  new IllegalStateException("Email taken");
            }
            voter.setEmail(email);
        }
    }

    @Override
    public Voter updateVoter(Long voterId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Voter foundVoter = getVoterById(voterId);
        JsonNode node = mapper.convertValue(foundVoter, JsonNode.class);
        try{
            JsonNode updateNode = updatePayload.apply(node);
            var updatedVoter = mapper.convertValue(updateNode, Voter.class);
            updatedVoter = voterRepository.save(updatedVoter);
            return updatedVoter;

        } catch (JsonPatchException exception){
            log.error(exception.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteVoter(Long voterId) {
        boolean isExist = voterRepository.existsById(voterId);
        if(!isExist){
            throw new IllegalStateException("Voter with id " + voterId + "does not exist");
        }
        voterRepository.deleteById(voterId);
    }

//    @Override
//    public VoteResponse castVote(VoteRequest voteRequest) {
//        Voter voter = voterRepository.findById(voteRequest.getVoterId())
//                .orElseThrow(() ->
//                        new VoterManagementException("Voter not found"));
//
//        if (voter.getVoterStatus() == VoterStatus.REGISTERED){
//            throw new VoterManagementException(
//                    String.format("%s has already voted", voter.getName()));
//        }
//        Candidate candidate = candidateRepository.findById(voteRequest.getCandidateId())
//                .orElseThrow(()-> new VoterManagementException("Candidate not found"));
//        Ballot ballot = ballotRepo.findById(voteRequest.getBallotId())
//                .orElseThrow(()->
//                        new VoterManagementException("Ballot not found"));
//        PollingUnit pollingUnit = pollingUnitRepository.findById(voteRequest.getPollingUnitId())
//                .orElseThrow(()-> new VoterManagementException("Polling unit not found"));
//        Vote vote = new Vote();
//        vote.setCandidate(candidate);
//        vote.setVoter(voter);
//        vote.setPollingUnit(pollingUnit);
//        vote.setBallot(ballot);
//        Vote savedVoted = voteRepository.save(vote);
//        voter.setVoterStatus(VoterStatus.VOTED);
//        voterRepository.save(voter);
//        pollingUnitRepository.save(pollingUnit);
//        ballotRepo.save(ballot);
//
//
//
//    }


}
