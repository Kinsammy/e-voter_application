package com.example.e_voter.service.implementations;

import com.example.e_voter.data.dto.request.RegisterCandidateRequest;
import com.example.e_voter.data.dto.request.RegisterVoterRequest;
import com.example.e_voter.data.models.Candidate;
import com.example.e_voter.exception.VoterManagementException;
import com.example.e_voter.service.CandidateService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.ReplaceOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CandidateServiceImplTest {
    @Autowired
    private CandidateService candidateService;
    private RegisterCandidateRequest request;
    @BeforeEach
    void setUp() {
        request = new RegisterCandidateRequest();
        request.setName("samuel");
        request.setParty("Labour Party");
    }

    @Test
    void registerTest() {
        var response = candidateService.register(request);
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void getAllCandidatesTest() {
        var response = candidateService.getAllCandidates();
        assertThat(response).isNotNull();
    }

    @Test
    void getCandidateByIdTest() {
        var response = candidateService.register(request);
        Candidate foundCandidate = candidateService.getCandidateById(response.getId());
        assertThat(foundCandidate).isNotNull();
        assertThat(foundCandidate.getParty()).isEqualTo(request.getParty());
    }

    @Test
    void updateCandidateTest() throws JsonPointerException {
        ObjectMapper mapper = new ObjectMapper();
        Candidate foundCandidate = new Candidate();
        JsonNode node = mapper.convertValue(foundCandidate, JsonNode.class);
        JsonPatch updatePayload = new JsonPatch(List.of(
                new ReplaceOperation(
                        new JsonPointer("/party"), node)
        ));
        var response = candidateService.register(request);
        var updatedCandidate = candidateService.updateCandidate(response.getId(), updatePayload);
        assertThat(updatedCandidate).isNotNull();
        assertThat(updatedCandidate.getParty()).isNotNull();
    }



    @Test
    void deleteCandidateTest() {
        var response = candidateService.register(request);
        candidateService.deleteCandidate(response.getId());
        assertThrows(VoterManagementException.class, ()->
                candidateService.getCandidateById(response.getId()));
    }
}