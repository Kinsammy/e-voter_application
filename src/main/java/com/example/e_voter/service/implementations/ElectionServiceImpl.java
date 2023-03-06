package com.example.e_voter.service.implementations;

import com.example.e_voter.data.dto.request.RegisterElectionRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.Election;
import com.example.e_voter.data.repositories.ElectionRepository;
import com.example.e_voter.exception.VoterManagementException;
import com.example.e_voter.service.ElectionService;
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
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository electionRepository;
    @Override
    public RegisterResponse register(RegisterElectionRequest registerRequest) {
        ModelMapper mapper = new ModelMapper();
        Election election = mapper.map(registerRequest, Election.class);
//        validateElection(election);
        election.setCreatedAt(LocalDateTime.now().toString());
        Election savedElection = electionRepository.save(election);
        RegisterResponse registerResponse = getRegisterResponse(savedElection);
        return registerResponse;
    }

//    private void validateElection(Election election) {
//        Optional<Election> electionOptional = electionRepository
//                .findByName(election.getName());
//        if (electionOptional.isPresent()){
//            throw new IllegalStateException(election.getName() + " Election is still  on");
//        }
//    }

    private RegisterResponse getRegisterResponse(Election savedElection) {
        RegisterResponse  registerResponse = new RegisterResponse();
        registerResponse.setId(savedElection.getId());
        registerResponse.setCode(HttpStatus.CREATED.value());
        registerResponse.setSuccess(true);
        registerResponse.setMessage("Election Registration Successful");
        return registerResponse;
    }

    @Override
    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    @Override
    public Election getElectionById(Long electionId) {
        return electionRepository.findById(electionId).orElseThrow(()->
                new VoterManagementException(
                        String.format("Election with id %d not found ", electionId)));
    }

    @Override
    public void updateElection(Long electionId, String name) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(()-> new IllegalStateException(
                        "Election with id " + electionId + "does not exit"
                ));

        if (name != null && name.length() > 0 && election.getName().equals(name)){
            election.setName(name);
        }
    }

    @Override
    public Election updateElection(Long electionId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Election foundElection = getElectionById(electionId);
        JsonNode node = mapper.convertValue(foundElection, JsonNode.class);
        try{
            JsonNode updateNode = updatePayload.apply(node);
            var updatedElection = mapper.convertValue(updateNode, Election.class);
            updatedElection = electionRepository.save(updatedElection);
            return updatedElection;
        } catch (JsonPatchException exception){
            log.error(exception.getMessage());
            throw new RuntimeException();
        }

    }

    @Override
    public void deleteElection(Long electionId) {
        boolean isExist = electionRepository.existsById(electionId);
        if (!isExist){
            throw new IllegalStateException("Election with id " + electionId + "does not exist");
        }
        electionRepository.deleteById(electionId);
    }
}
