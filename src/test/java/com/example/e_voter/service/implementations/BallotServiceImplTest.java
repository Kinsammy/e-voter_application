package com.example.e_voter.service.implementations;

import com.example.e_voter.data.dto.request.RegisterBallotRequest;
import com.example.e_voter.data.models.Ballot;
import com.example.e_voter.data.models.Candidate;
import com.example.e_voter.data.models.Election;
import com.example.e_voter.exception.VoterManagementException;
import com.example.e_voter.service.BallotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BallotServiceImplTest {
    @Autowired
    private  BallotService ballotService;
    private RegisterBallotRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterBallotRequest();
        request.setCandidates(new Candidate[]{} );
        request.setElections(
                new Election[]{}
        );
    }

    @Test
    void registerTest() {
        var response = ballotService.register(request);
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void getAllBallotsTest() {
        var response = ballotService.getAllBallots();
            assertThat(response).isNotNull();

    }

    @Test
    void getBallotByIdTest() {
        var response = ballotService.register(request);
        Ballot foundBallot = ballotService.getBallotById(response.getId());
        assertThat(foundBallot).isNotNull();
        assertThat(foundBallot.getCandidates()).containsExactly(request.getCandidates());
    }

    @Test
    void updateBallot() {
    }

    @Test
    void deleteBallot() {
        var response = ballotService.register(request);
        ballotService.deleteBallot(response.getId());
        assertThrows(VoterManagementException.class, ()->
                ballotService.getBallotById(response.getId()));
    }
}