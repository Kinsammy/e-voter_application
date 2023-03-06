package com.example.e_voter.service.implementations;

import com.example.e_voter.data.dto.request.RegisterCandidateRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.Candidate;
import com.example.e_voter.data.models.Voter;
import com.example.e_voter.data.repositories.CandidateRepository;
import com.example.e_voter.exception.VoterManagementException;
import com.example.e_voter.service.CandidateService;
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

@Service
@AllArgsConstructor
@Slf4j
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    @Override
    public RegisterResponse register(RegisterCandidateRequest registerRequest) {
        ModelMapper mapper = new ModelMapper();
        Candidate candidate = mapper.map(registerRequest, Candidate.class);
        candidate.setCreatedAt(LocalDateTime.now().toString());
        Candidate savedCandidate = candidateRepository.save(candidate);
        RegisterResponse response = getRegisterResponse(savedCandidate);
        return response;
    }

    private RegisterResponse getRegisterResponse(Candidate savedCandidate) {
        var response = new RegisterResponse();
        response.setId(savedCandidate.getId());
        response.setCode(HttpStatus.CREATED.value());
        response.setSuccess(true);
        response.setMessage("Candidate Registration Successful");
        return response;
    }

    @Override
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate getCandidateById(Long candidateId) {
        return candidateRepository.findById(candidateId).orElseThrow(()->
                new VoterManagementException(
                        String.format("Candidate with id %d not found", candidateId)));
    }

    @Transactional
    @Override
    public void updateCandidate(Long candidateId, String name, String party) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(()-> new IllegalStateException(
                        String.format("Candidate with id %d does not exist", candidateId)
                ));
        if (name != null && name.length() > 0 && candidate.getName().equals(name)){
            candidate.setName(name);
        }

        if (party != null && party.length() > 0 && candidate.getParty().equals(party)){
            candidate.setParty(party);
        }
    }

    @Override
    public Candidate updateCandidate(Long candidateId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Candidate foundCandidate = getCandidateById(candidateId);
        JsonNode node = mapper.convertValue(foundCandidate, JsonNode.class);
        try {
            JsonNode updateNode = updatePayload.apply(node);
            var updatedCandidate = mapper.convertValue(foundCandidate, Candidate.class);
            updatedCandidate = candidateRepository.save(updatedCandidate);
            return updatedCandidate;
        } catch (JsonPatchException exception){
            log.error(exception.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteCandidate(Long candidateId) {
        boolean isExist = candidateRepository.existsById(candidateId);
        if (!isExist){
            throw new IllegalStateException(
                    String.format("Candidate with id %d does not exist", candidateId)
            );
        }
        candidateRepository.deleteById(candidateId);
    }
}
