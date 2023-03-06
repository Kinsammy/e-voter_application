package com.example.e_voter.data.dto.request;

import com.example.e_voter.data.models.Candidate;
import com.example.e_voter.data.models.Election;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterBallotRequest {
    private Election[] elections;
    private Candidate[] candidates;
}
