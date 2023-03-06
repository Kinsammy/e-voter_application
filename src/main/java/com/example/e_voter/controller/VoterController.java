package com.example.e_voter.controller;

import com.example.e_voter.data.dto.request.RegisterVoterRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.Voter;
import com.example.e_voter.service.VoterService;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/voter")
@AllArgsConstructor
public class VoterController {
    private final VoterService voterService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterVoterRequest registerVoterRequest){
        RegisterResponse registerResponse = voterService.register(registerVoterRequest);
        return ResponseEntity.status(registerResponse.getCode()).body(registerResponse);
    }

    @GetMapping
    public List<Voter> getAllVoters(){
        return voterService.getAllVoters();
    }

    @GetMapping("{voterId}")
    public ResponseEntity<?> getVoterById(@PathVariable Long voterId){
        var foundVoter = voterService.getVoterById(voterId);
        return ResponseEntity.status(HttpStatus.OK).body(foundVoter);
    }

    @PutMapping("{voterId}")
    public void updateVoter(@PathVariable("voterId") Long voterId,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) String email){
        voterService.updateVoter(voterId, name, email);
    }

    @PatchMapping(value = "{voterId}", consumes = {"application/json-patch+json"})
    public ResponseEntity<?> updateVoter(@PathVariable Long voterId, @RequestBody JsonPatch updatePatch){
        try {
            var response = voterService.updateVoter(voterId, updatePatch);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("{voterId}")
    public ResponseEntity<?> deleteVoter(@PathVariable Long voterId){
        voterService.deleteVoter(voterId);
        return ResponseEntity.ok("Voter deleted successfully");
    }



}
