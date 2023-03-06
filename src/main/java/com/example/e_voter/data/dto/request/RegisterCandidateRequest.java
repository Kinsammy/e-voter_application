package com.example.e_voter.data.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterCandidateRequest {
    private String name;
    private String party;
}
