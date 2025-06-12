package com.api.rescuemeapi.api.controllers;

import com.api.rescuemeapi.domain.dtos.petition.CreatePetitionRequest;
import com.api.rescuemeapi.domain.dtos.petition.PetitionResponse;
import com.api.rescuemeapi.domain.dtos.petition.UpdatePetitionRequest;
import com.api.rescuemeapi.application.services.PetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/petitions")
@RequiredArgsConstructor
public class PetitionController {

    private final PetitionService petitionService;

    @GetMapping("/user")
    public ResponseEntity<List<PetitionResponse>> getPetitionsByUser() {
        List<PetitionResponse> response = petitionService.getAllByUser();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pet/{id}")
    public ResponseEntity<List<PetitionResponse>> getPetitionsByPet(@PathVariable Long id) {
        List<PetitionResponse> response = petitionService.getAllByPet(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PetitionResponse> createPetition(@Valid @RequestBody CreatePetitionRequest request) {
        PetitionResponse response = petitionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetitionResponse> updatePetition(@PathVariable Long id,
                                                           @Valid @RequestBody UpdatePetitionRequest request) {
        PetitionResponse response = petitionService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PetitionResponse> approvePetition(@PathVariable Long id) {
        PetitionResponse response = petitionService.approve(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<PetitionResponse> declinePetition(@PathVariable Long id) {
        PetitionResponse response = petitionService.decline(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetition(@PathVariable Long id) {
        petitionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
