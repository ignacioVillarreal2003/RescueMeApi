package com.api.rescuemeapi.api.controllers;

import com.api.rescuemeapi.domain.dtos.pet.*;
import com.api.rescuemeapi.application.services.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping()
    public ResponseEntity<Page<PetResponse>> getPets(@ModelAttribute PetFilterRequest filter) {
        Page<PetResponse> response = petService.getAll(filter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getPet(@PathVariable Long id) {
        PetResponse response = petService.get(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PetResponse> createPet(@Valid @ModelAttribute CreatePetRequest request) {
        PetResponse response = petService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long id,
                                                 @Valid @ModelAttribute UpdatePetRequest request) {
        PetResponse response = petService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/adopt/user/{userId}")
    public ResponseEntity<PetResponse> adoptPet(@PathVariable Long id, @PathVariable Long userId) {
        PetResponse response = petService.adopt(id, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
