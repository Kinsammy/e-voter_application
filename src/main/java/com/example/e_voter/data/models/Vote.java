package com.example.e_voter.data.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "voter_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Voter voter;

    @JoinColumn(name = "candidate_id")
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Candidate candidate;

    @JoinColumn(name = "ballot_id")
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Ballot ballot;

    @JoinColumn(name = "pollingunit_id")
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private PollingUnit pollingUnit;

    private boolean isValid;
    @Enumerated(EnumType.STRING)
    private VoteStatus voteStatus;
    private String castedVoteOn;


}
