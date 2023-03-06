package com.example.e_voter.service.implementations;

import com.example.e_voter.data.dto.request.RegisterVoterRequest;
import com.example.e_voter.data.models.Voter;
import com.example.e_voter.exception.VoterManagementException;
import com.example.e_voter.service.VoterService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
class VoterServiceImplTest {

    @Autowired
    private VoterService voterService;
    private RegisterVoterRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterVoterRequest();
        request.setEmail("test@gmail.com");
        request.setName("samuel");
        request.setPassword("testPassword");
    }

    @Test
    void registerTest() {
        var registerResponse = voterService.register(request);
        assertThat(registerResponse).isNotNull();
        assertThat(registerResponse.getCode())
                .isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void getAllVotersTest() {
        var registerResponse = voterService.getAllVoters();
        assertThat(registerResponse).isNotNull();
    }

    @Test
    void getVoterById() {
        var registerResponse = voterService.register(request);
        Voter foundVoter = voterService.getVoterById(registerResponse.getId());
        assertThat(foundVoter).isNotNull();
        assertThat(foundVoter.getName()).isEqualTo(request.getName());
    }

    @Test
    void updateVoterTest() throws JsonProcessingException, JsonPointerException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("2348102402753");
        JsonPatch updatePayload = new JsonPatch(List.of(
                new ReplaceOperation(
                        new JsonPointer("/phoneNumber"), node)
        ));
        var registerResponse = voterService.register(request);
        var updatedVoter = voterService.updateVoter(registerResponse.getId(), updatePayload);
        assertThat(updatedVoter).isNotNull();
        assertThat(updatedVoter.getPhoneNumber()).isNotNull();
    }

    @Test
    void deleteVoter() {
        var registerResponse = voterService.register(request);
        voterService.deleteVoter(registerResponse.getId());
        assertThrows(VoterManagementException.class, ()->
                voterService.getVoterById(registerResponse.getId()));
    }
}