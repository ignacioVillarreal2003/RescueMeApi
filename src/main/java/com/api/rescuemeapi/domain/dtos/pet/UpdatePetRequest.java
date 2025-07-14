package com.api.rescuemeapi.domain.dtos.pet;

import com.api.rescuemeapi.domain.constants.PetSex;
import com.api.rescuemeapi.domain.constants.PetSize;
import com.api.rescuemeapi.domain.constants.PetSpecies;
import com.api.rescuemeapi.domain.constants.PetState;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record UpdatePetRequest (
        @Size(max = 64, message = "Name must be less than 64 characters")
        String name,

        @Size(max = 255, message = "Description must be less than 255 characters")
        String description,

        @Range(min = 0, max = 30, message = "Age must be between 0 to 30 characters")
        Integer age,

        PetSpecies species,

        PetSize size,

        PetSex sex,

        @Size(max = 64, message = "Breed must be less than 64 characters")
        String breed,

        @Size(max = 64, message = "Color must be less than 64 characters")
        String color,

        Boolean isVaccinated,

        Boolean isCastrated,

        Boolean isDewormed,

        @Size(max = 255, message = "Medical notes must be less than 255 characters")
        String medicalNotes,

        PetState state
) {
}
