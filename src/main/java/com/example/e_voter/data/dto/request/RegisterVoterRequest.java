package com.example.e_voter.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterVoterRequest {
    private String email;
    @JsonProperty("full_name")
    private String name;
    @JsonProperty("password")
    private String password;
}
