package com.example.e_voter.controller;

import com.example.e_voter.data.dto.request.RegisterCandidateRequest;
import com.example.e_voter.data.dto.response.RegisterResponse;
import com.example.e_voter.data.models.Candidate;
import com.example.e_voter.service.CandidateService;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v3/candidate")
@AllArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterCandidateRequest request){
        RegisterResponse response = candidateService.register(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping
    public List<Candidate> getAllCandidate(){
        return candidateService.getAllCandidates();
    }

    @GetMapping("{candidateId}")
    public ResponseEntity<?> getCandidateById(@PathVariable Long candidateId){
        var foundCandidate = candidateService.getCandidateById(candidateId);
        return ResponseEntity.status(HttpStatus.OK).body(foundCandidate);
    }

    @PutMapping("{candidateId}")
    public void updateCandidate(@PathVariable Long candidateId,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String party){
        candidateService.updateCandidate(candidateId, name, party);
    }

    @PatchMapping(value = "{candidateId}", consumes = {"application/json-patch+json"})
    public ResponseEntity<?> updateCandidate(@PathVariable Long candidateId, @RequestBody JsonPatch updatePayload) {
        try {
            var response = candidateService.updateCandidate(candidateId, updatePayload);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("{candidateId}")
    public ResponseEntity<?> deleteCandidate(@PathVariable Long candidateId){
        candidateService.deleteCandidate(candidateId);
        return ResponseEntity.ok("Candidate deleted successfully");
    }

}
