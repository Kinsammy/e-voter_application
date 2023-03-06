package com.example.e_voter.service;

import com.example.e_voter.data.dto.request.VoteRequest;
import com.example.e_voter.data.dto.response.VoteResponse;
import com.example.e_voter.data.models.*;

public interface VoteService {
    VoteResponse castVote(VoteRequest voteRequest);
}
