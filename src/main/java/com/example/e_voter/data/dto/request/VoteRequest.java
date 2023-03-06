package com.example.e_voter.data.dto.request;

import com.example.e_voter.data.models.Ballot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VoteRequest {
    private String party;
    private Long voterId;
    private Long candidateId;
    private Long ballotId;
    private Long pollingUnitId;

}
