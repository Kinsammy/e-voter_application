package com.example.e_voter.service.implementations;

import com.example.e_voter.data.dto.request.RegisterElectionRequest;
import com.example.e_voter.data.models.Election;
import com.example.e_voter.exception.VoterManagementException;
import com.example.e_voter.service.ElectionService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ElectionServiceImplTest {

    @Autowired
    private ElectionService electionService;
    private RegisterElectionRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterElectionRequest();
        request.setName("Presidential Election");
    }

    @Test
    void registerTest() {
        var response = electionService.register(request);
        assertThat(response).isNotNull();
        assertThat(response.getCode())
                .isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void getAllElectionsTest() {
        var response = electionService.getAllElections();
        assertThat(response).isNotNull();
    }

    @Test
    void getElectionById() {
        var response = electionService.register(request);
        Election foundElection = electionService.getElectionById(response.getId());
        assertThat(foundElection).isNotNull();
        assertThat(foundElection.getName()).isEqualTo(request.getName());
    }

    @Test
    void updateElection() throws JsonPointerException {
        ObjectMapper mapper = new ObjectMapper();
        Election foundElection = new Election();
        JsonNode node = mapper.convertValue(foundElection, JsonNode.class);
        JsonPatch updatedPayload = new JsonPatch(List.of(
                new ReplaceOperation(
                        new JsonPointer("/"), node)
        ));
        var response = electionService.register(request);
        var updatedElection = electionService.updateElection(response.getId(), updatedPayload);
        assertThat(updatedElection).isNotNull();
        assertThat(updatedElection.getName()).isNotNull();
    }

    @Test
    void deleteElectionTest() {
        var response = electionService.register(request);
        electionService.deleteElection(response.getId());
        assertThrows(VoterManagementException.class, ()->
                electionService.getElectionById(response.getId()));
    }
}