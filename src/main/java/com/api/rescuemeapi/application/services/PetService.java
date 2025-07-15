package com.api.rescuemeapi.application.services;

import com.api.rescuemeapi.application.helpers.PetHelperService;
import com.api.rescuemeapi.application.helpers.PetitionHelperService;
import com.api.rescuemeapi.application.helpers.UserHelperService;
import com.api.rescuemeapi.application.mappers.PetResponseMapper;
import com.api.rescuemeapi.config.authentication.AuthenticationUserProvider;
import com.api.rescuemeapi.domain.dtos.pet.*;
import com.api.rescuemeapi.domain.constants.PetState;
import com.api.rescuemeapi.domain.models.Pet;
import com.api.rescuemeapi.domain.models.User;
import com.api.rescuemeapi.infrastructure.persistence.repositories.PetRepository;
import com.api.rescuemeapi.infrastructure.persistence.specification.PetSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final PetResponseMapper petResponseMapper;
    private final AuthenticationUserProvider authenticationUserProvider;
    private final PetHelperService petHelper;
    private final UserHelperService userHelper;
    private final PetitionHelperService petitionHelper;

    public Page<PetResponse> getAllPets(PetFilterRequest filter) {
        Specification<Pet> spec = getSpecification(filter);

        return petRepository.findAll(spec, filter.getPageable())
                .map(petResponseMapper);
    }

    private Specification<Pet> getSpecification(PetFilterRequest filter) {
        Specification<Pet> spec = Specification.where(PetSpecification.isAvailable());
        if (filter.getIsOwned() != null && filter.getIsOwned()) {
            String currentUserEmail = authenticationUserProvider.getUser().getEmail();
            spec = spec.and(PetSpecification.isOwned(currentUserEmail));
        }
        if (filter.getSpeciesList() != null && !filter.getSpeciesList().isEmpty()) {
            spec = spec.and(PetSpecification.hasSpeciesIn(filter.getSpeciesList()));
        }
        if (filter.getAge() != null) {
            spec = spec.and(PetSpecification.hasAge(filter.getAge()));
        }
        if (filter.getSizeList() != null && !filter.getSizeList().isEmpty()) {
            spec = spec.and(PetSpecification.hasSizeIn(filter.getSizeList()));
        }
        if (filter.getSexList() != null && !filter.getSexList().isEmpty()) {
            spec = spec.and(PetSpecification.hasSexIn(filter.getSexList()));
        }
        return spec;
    }

    public PetResponse getPet(Long id) {
        Pet pet = petHelper.findById(id);
        return petResponseMapper.apply(pet);
    }

    public PetResponse createPet(CreatePetRequest request) {
        User user = userHelper.getCurrentUser();

        UUID referenceId = UUID.randomUUID();

        Pet createdPet = petRepository.save(
                Pet.builder()
                        .name(request.name())
                        .description(request.description())
                        .species(request.species())
                        .age(request.age())
                        .size(request.size())
                        .sex(request.sex())
                        .ownerUser(user)
                        .referenceId(referenceId)
                        .build());

        return petResponseMapper.apply(createdPet);
    }

    public PetResponse updatePet(Long id, UpdatePetRequest request) {
        Pet pet = petHelper.findById(id);

        if (!petHelper.isOwner(pet)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the pet owner may update this pet");
        }

        if (request.name() != null) pet.setName(request.name());
        if (request.description() != null) pet.setDescription(request.description());
        if (request.species() != null) pet.setSpecies(request.species());
        if (request.age() != null) pet.setAge(request.age());
        if (request.size() != null) pet.setSize(request.size());
        if (request.sex() != null) pet.setSex(request.sex());
        if (request.breed() != null) pet.setBreed(request.breed());
        if (request.color() != null) pet.setColor(request.color());
        if (request.isVaccinated() != null) pet.setIsVaccinated(request.isVaccinated());
        if (request.isCastrated() != null) pet.setIsCastrated(request.isCastrated());
        if (request.isDewormed() != null) pet.setIsDewormed(request.isDewormed());
        if (request.medicalNotes() != null) pet.setMedicalNotes(request.medicalNotes());

        Pet updated = petRepository.save(pet);

        return petResponseMapper.apply(updated);
    }

    @Transactional
    public PetResponse adoptPet(Long id, String email) {
        Pet pet = petHelper.findById(id);

        if (!petHelper.isOwner(pet)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the pet owner may update this pet");
        }

        pet.setState(PetState.ADOPTED);

        petitionHelper.declineAllExcept(pet, email);

        Pet updated = petRepository.save(pet);

        return petResponseMapper.apply(updated);
    }

    public void deletePet(Long id) {
        Pet pet = petHelper.findById(id);

        if (!petHelper.isOwner(pet)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only the pet owner may delete this petition");
        }

        petRepository.delete(pet);
    }
}
