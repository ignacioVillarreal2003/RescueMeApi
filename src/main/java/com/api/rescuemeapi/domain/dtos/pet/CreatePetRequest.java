package com.api.rescuemeapi.domain.dtos.pet;

import com.api.rescuemeapi.domain.enums.PetSex;
import com.api.rescuemeapi.domain.enums.PetSize;
import com.api.rescuemeapi.domain.enums.PetSpecies;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreatePetRequest (
        @NotNull(message = "Name is required")
        @Size(max = 64, message = "Name must be less than 64 characters")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "Name must only contain letters")
        String name,

        @NotNull(message = "Description is required")
        @Size(max = 255, message = "Description must be less than 255 characters")
        String description,

        @NotNull(message = "Age is required")
        @Range(min = 0, max = 30, message = "Age must be between 0 to 30 characters")
        int age,

        @NotNull(message = "Species is required")
        PetSpecies species,

        @NotNull(message = "Size is required")
        PetSize size,

        @NotNull(message = "Sex is required")
        PetSex sex,

        @NotNull(message = "Files is required")
        List<MultipartFile> files
) {
}
