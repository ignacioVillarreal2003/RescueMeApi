package com.api.rescuemeapi.application.helpers;

import com.api.rescuemeapi.config.authentication.AuthUserProvider;
import com.api.rescuemeapi.domain.enums.PetState;
import com.api.rescuemeapi.domain.models.Pet;
import com.api.rescuemeapi.infrastructure.persistence.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetHelper {

    private final PetRepository petRepository;
    private final AuthUserProvider authUserProvider;

    public Optional<Pet> findById(Long id) {
        return petRepository.findById(id);
    }

    public boolean isAvailable(Pet pet) {
        return pet.getState() == PetState.AVAILABLE;
    }

    public boolean isAdopted(Pet pet) {
        return pet.getState() == PetState.ADOPTED;
    }

    public boolean isOwner(Pet pet) {
        return pet.getOwnerUser()
                .getId()
                .equals(authUserProvider.getUserId());
    }
}
