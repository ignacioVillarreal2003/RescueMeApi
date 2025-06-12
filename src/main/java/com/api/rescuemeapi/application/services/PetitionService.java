package com.api.rescuemeapi.application.services;

import com.api.rescuemeapi.application.helpers.PetHelper;
import com.api.rescuemeapi.application.helpers.PetitionHelper;
import com.api.rescuemeapi.application.helpers.UserHelper;
import com.api.rescuemeapi.application.mappers.PetitionResponseMapper;
import com.api.rescuemeapi.domain.models.Pet;
import com.api.rescuemeapi.domain.models.User;
import com.api.rescuemeapi.domain.enums.PetitionStatus;
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
    private final PetitionHelper petitionHelper;
    private final UserHelper userHelper;
    private final PetHelper petHelper;
    private final PetitionResponseMapper petitionResponseMapper;

    public List<PetitionResponse> getAllByUser() {
        User user = userHelper.getCurrentUser()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Authenticated user not found"));

        return petitionRepository.findAllByRequestingUser(user)
                .stream()
                .map(petitionResponseMapper)
                .collect(Collectors.toList());
    }

    public List<PetitionResponse> getAllByPet(Long petId) {
        Pet pet = petHelper.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pet not found"));

        if (!petHelper.isOwner(pet)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not the owner of this pet");
        }

        return petitionRepository.findAllByRequestedPet(pet)
                .stream()
                .map(petitionResponseMapper)
                .collect(Collectors.toList());
    }

    public PetitionResponse create(CreatePetitionRequest request) {
        User user = userHelper.getCurrentUser()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Authenticated user not found"));

        Pet pet = petHelper.findById(request.petId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pet not found"));

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

    public PetitionResponse update(Long id, UpdatePetitionRequest request) {
        Petition petition = petitionHelper.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Petition not found"));

        if (!petitionHelper.isRequestingUser(petition)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the petitioner may update this petition");
        }

        if (request.message() != null) petition.setMessage(request.message());

        Petition updated = petitionRepository.save(petition);

        return petitionResponseMapper.apply(updated);
    }

    public PetitionResponse approve(Long id) {
        Petition petition = petitionHelper.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Petition not found"));

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

    public PetitionResponse decline(Long id) {
        Petition petition = petitionHelper.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Petition not found"));

        if (!petitionHelper.isPetOwner(petition)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the pet owner may decline petitions");
        }

        petition.setStatus(PetitionStatus.DECLINED);

        Petition updatedPetition = petitionRepository.save(petition);

        return petitionResponseMapper.apply(updatedPetition);
    }

    public void delete(Long id) {
        Petition petition = petitionHelper.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Petition not found"));

        if (!petitionHelper.isRequestingUser(petition)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the petitioner may delete this petition");
        }

        petitionRepository.delete(petition);
    }
}
