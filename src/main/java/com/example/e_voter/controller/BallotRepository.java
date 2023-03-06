package com.example.e_voter.controller;


import com.example.e_voter.data.dto.request.RegisterBallotRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.Ballot;
import com.example.e_voter.service.BallotService;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v4/ballot")
@AllArgsConstructor
public class BallotRepository {
    private final BallotService ballotService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterBallotRequest request){
        RegisterResponse registerResponse = ballotService.register(request);
        return ResponseEntity.status(registerResponse.getCode()).body(registerResponse);
    }

    @GetMapping
    public List<Ballot> getAllBallot(){
        return ballotService.getAllBallots();
    }

    @GetMapping("{ballotId}")
    public ResponseEntity<?> getBallotById(@PathVariable Long ballotId){
        var foundVoter = ballotService.getBallotById(ballotId);
        return ResponseEntity.status(HttpStatus.OK).body(foundVoter);
    }


    @PatchMapping(value = "{ballotId}", consumes = {"application/json-patch+json"})
    public ResponseEntity<?> updateVoter(@PathVariable Long ballotId, @RequestBody JsonPatch updatePatch){
        try {
            var response = ballotService.updateBallot(ballotId, updatePatch);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("{ballotId}")
    public ResponseEntity<?> deleteVoter(@PathVariable Long ballotId){
        ballotService.deleteBallot(ballotId);
        return ResponseEntity.ok("Voter deleted successfully");
    }

    @PostMapping("/open")
    public ResponseEntity<String> openBallot(@RequestBody Ballot ballot){
        try{
            ballotService.openBallot(ballot);
            return ResponseEntity.ok("Ballot opened successfully");
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            String.format("Error opening ballot %s", exception.getMessage())
                    );
        }
    }
}
