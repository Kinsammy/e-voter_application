package com.example.e_voter.data.dto.response;

import com.example.e_voter.data.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VoteResponse {
    private Long id;
    private Voter voter;
    private Candidate candidate;
    private PollingUnit pollingUnit;
    private Ballot ballot;
    private String votedOn;
    private VoteStatus voteStatus;
}
