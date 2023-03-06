package com.example.e_voter.controller;

import com.example.e_voter.data.dto.request.RegisterElectionRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.Election;
import com.example.e_voter.service.ElectionService;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/election")
@AllArgsConstructor
public class ElectionController {
    private final ElectionService electionService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterElectionRequest registerElectionRequest){
        RegisterResponse registerResponse = electionService.register(registerElectionRequest);
        return ResponseEntity.status(registerResponse.getCode()).body(registerResponse);
    }

    @GetMapping
    public List<Election> getAllElections(){
        return electionService.getAllElections();

    }

    @GetMapping("{electionId}")
    private ResponseEntity<?> getElectionById(@PathVariable Long electionId){
        var foundElection = electionService.getElectionById(electionId);
        return ResponseEntity.status(HttpStatus.OK).body(foundElection);
    }

    @PutMapping("{electionId}")
    private void updateElection(@PathVariable("electionId") Long electionId,
                                             @RequestParam(required = false) String name){
       electionService.updateElection(electionId, name);
    }

    @PatchMapping(value = "{electionId}", consumes = {"application/json-patch+json"})
    public ResponseEntity<?> updateElection(@PathVariable Long electionId, @RequestBody JsonPatch updatePatch){
        try {
            var response = electionService.updateElection(electionId, updatePatch);
            return  ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("{electionId}")
    public  ResponseEntity<?> deleteElection(@PathVariable Long electionId){
        electionService.deleteElection(electionId);
        return ResponseEntity.ok("Election deleted successfully");
    }

}
