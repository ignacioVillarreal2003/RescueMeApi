package com.api.rescuemeapi.application.helpers;

import com.api.rescuemeapi.application.exceptions.PetNotFoundException;
import com.api.rescuemeapi.config.authentication.AuthenticationUserProvider;
import com.api.rescuemeapi.domain.constants.PetState;
import com.api.rescuemeapi.domain.models.Pet;
import com.api.rescuemeapi.infrastructure.persistence.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetHelperService {

    private final PetRepository petRepository;
    private final AuthenticationUserProvider authenticationUserProvider;

    public Pet findById(Long id) {
        return petRepository.findById(id).orElseThrow(PetNotFoundException::new);
    }

    public boolean isAvailable(Pet pet) {
        return pet.getState() == PetState.AVAILABLE;
    }

    public boolean isAdopted(Pet pet) {
        return pet.getState() == PetState.ADOPTED;
    }

    public boolean isOwner(Pet pet) {
        return pet.getOwnerUser()
                .getEmail()
                .equals(authenticationUserProvider.getUser().getEmail());
    }
}
