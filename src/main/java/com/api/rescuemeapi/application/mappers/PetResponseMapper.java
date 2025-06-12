package com.api.rescuemeapi.application.mappers;

import com.api.rescuemeapi.domain.dtos.pet.PetResponse;
import com.api.rescuemeapi.domain.models.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PetResponseMapper implements Function<Pet, PetResponse> {

    private final UserResponseMapper userResponseMapper;

    @Override
    public PetResponse apply(Pet pet) {
        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .description(pet.getDescription())
                .species(pet.getSpecies())
                .age(pet.getAge())
                .size(pet.getSize())
                .state(pet.getState())
                .sex(pet.getSex())
                .breed(pet.getBreed())
                .color(pet.getColor())
                .isVaccinated(pet.getIsVaccinated())
                .isCastrated(pet.getIsCastrated())
                .isDewormed(pet.getIsDewormed())
                .medicalNotes(pet.getMedicalNotes())
                .ownerUser(userResponseMapper.apply(pet.getOwnerUser()))
                .build();
    }
}
