package com.api.rescuemeapi.application.helpers;

import com.api.rescuemeapi.config.authentication.AuthUserProvider;
import com.api.rescuemeapi.domain.enums.PetitionStatus;
import com.api.rescuemeapi.domain.models.Pet;
import com.api.rescuemeapi.domain.models.Petition;
import com.api.rescuemeapi.infrastructure.persistence.repositories.PetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetitionHelper {

    private final PetitionRepository petitionRepository;
    private final AuthUserProvider authUserProvider;

    public Optional<Petition> findById(Long id) {
        return petitionRepository.findById(id);
    }

    public boolean isRequestingUser(Petition petition) {
        return petition.getRequestingUser()
                .getId()
                .equals(authUserProvider.getUserId());
    }

    public boolean isPetOwner(Petition petition) {
        return petition.getRequestedPet()
                .getOwnerUser()
                .getId()
                .equals(authUserProvider.getUserId());
    }

    public void declineAllExcept(Pet pet, Long userId) {
        List<Petition> petitions = petitionRepository.findAllByRequestedPet(pet)
                .stream()
                .filter(petition ->
                        !petition.getRequestingUser().getId().equals(userId))
                .toList();
        petitions.forEach(petition -> {
            petition.setStatus(PetitionStatus.DECLINED);
            petitionRepository.save(petition);
        });
    }
}
