package com.api.rescuemeapi.application.helpers;

import com.api.rescuemeapi.application.exceptions.PetitionNotFoundException;
import com.api.rescuemeapi.config.authentication.AuthenticationUserProvider;
import com.api.rescuemeapi.domain.constants.PetitionStatus;
import com.api.rescuemeapi.domain.models.Pet;
import com.api.rescuemeapi.domain.models.Petition;
import com.api.rescuemeapi.infrastructure.persistence.repositories.PetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetitionHelperService {

    private final PetitionRepository petitionRepository;
    private final AuthenticationUserProvider authenticationUserProvider;

    public Petition findById(Long id) {
        return petitionRepository.findById(id).orElseThrow(PetitionNotFoundException::new);
    }

    public boolean isRequestingUser(Petition petition) {
        return petition.getRequestingUser()
                .getEmail()
                .equals(authenticationUserProvider.getUser().getEmail());
    }

    public boolean isPetOwner(Petition petition) {
        return petition.getRequestedPet()
                .getOwnerUser()
                .getEmail()
                .equals(authenticationUserProvider.getUser().getEmail());
    }

    @Transactional
    public void declineAllExcept(Pet pet, String email) {
        List<Petition> petitions = petitionRepository.findAllByRequestedPet(pet)
                .stream()
                .filter(petition ->
                        !petition.getRequestingUser().getEmail().equals(email))
                .toList();

        petitions.forEach(petition -> {
            petition.setStatus(PetitionStatus.DECLINED);
        });

        petitionRepository.saveAll(petitions);
    }
}
