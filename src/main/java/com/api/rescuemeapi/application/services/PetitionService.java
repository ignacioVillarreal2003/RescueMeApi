package com.api.rescuemeapi.application.services;

import com.api.rescuemeapi.application.helpers.PetHelperService;
import com.api.rescuemeapi.application.helpers.PetitionHelperService;
import com.api.rescuemeapi.application.helpers.UserHelperService;
import com.api.rescuemeapi.application.mappers.PetitionResponseMapper;
import com.api.rescuemeapi.domain.models.Pet;
import com.api.rescuemeapi.domain.models.User;
import com.api.rescuemeapi.domain.constants.PetitionStatus;
import com.api.rescuemeapi.domain.dtos.petition.CreatePetitionRequest;
import com.api.rescuemeapi.domain.dtos.petition.PetitionResponse;
import com.api.rescuemeapi.domain.models.Petition;
import com.api.rescuemeapi.domain.dtos.petition.UpdatePetitionRequest;
import com.api.rescuemeapi.infrastructure.persistence.repositories.PetitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetitionService {

    private final PetitionRepository petitionRepository;
    private final PetitionHelperService petitionHelper;
    private final UserHelperService userHelper;
    private final PetHelperService petHelper;
    private final PetitionResponseMapper petitionResponseMapper;

    public List<PetitionResponse> getAllPetitionsByUser() {
        User user = userHelper.getCurrentUser();

        return petitionRepository.findAllByRequestingUser(user)
                .stream()
                .map(petitionResponseMapper)
                .collect(Collectors.toList());
    }

    public List<PetitionResponse> getAllPetitionsByPet(Long petId) {
        Pet pet = petHelper.findById(petId);

        if (!petHelper.isOwner(pet)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not the owner of this pet");
        }

        return petitionRepository.findAllByRequestedPet(pet)
                .stream()
                .map(petitionResponseMapper)
                .collect(Collectors.toList());
    }

    public PetitionResponse createPetition(CreatePetitionRequest request) {
        User user = userHelper.getCurrentUser();

        Pet pet = petHelper.findById(request.petId());

        if (petHelper.isAdopted(pet)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Cannot petition for an already adopted pet");
        }

        if (petHelper.isOwner(pet)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Cannot petition your own pet");
        }

        boolean exists = petitionRepository
                .existsByRequestedPetAndRequestingUser(pet, user);
        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "You already have an active petition for this pet");
        }

        Petition petition = petitionRepository.save(
                Petition.builder()
                        .status(PetitionStatus.PENDING)
                        .message(request.message())
                        .requestedPet(pet)
                        .requestingUser(user)
                        .build());

        Petition saved = petitionRepository.save(petition);

        return petitionResponseMapper.apply(saved);
    }

    public PetitionResponse updatePetition(Long id, UpdatePetitionRequest request) {
        Petition petition = petitionHelper.findById(id);

        if (!petitionHelper.isRequestingUser(petition)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the petitioner may update this petition");
        }

        if (request.message() != null) petition.setMessage(request.message());

        Petition updated = petitionRepository.save(petition);

        return petitionResponseMapper.apply(updated);
    }

    public PetitionResponse approvePetition(Long id) {
        Petition petition = petitionHelper.findById(id);

        if (!petitionHelper.isPetOwner(petition)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the pet owner may approve petitions");
        }

        Pet pet = petition.getRequestedPet();
        if (petHelper.isAdopted(pet)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Pet is already adopted");
        }

        petition.setStatus(PetitionStatus.APPROVED);

        Petition updated = petitionRepository.save(petition);

        return petitionResponseMapper.apply(updated);
    }

    public PetitionResponse declinePetition(Long id) {
        Petition petition = petitionHelper.findById(id);

        if (!petitionHelper.isPetOwner(petition)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the pet owner may decline petitions");
        }

        petition.setStatus(PetitionStatus.DECLINED);

        Petition updatedPetition = petitionRepository.save(petition);

        return petitionResponseMapper.apply(updatedPetition);
    }

    public void deletePetition(Long id) {
        Petition petition = petitionHelper.findById(id);

        if (!petitionHelper.isRequestingUser(petition)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the petitioner may delete this petition");
        }

        petitionRepository.delete(petition);
    }
}
