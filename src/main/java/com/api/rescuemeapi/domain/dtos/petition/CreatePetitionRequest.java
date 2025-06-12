package com.api.rescuemeapi.domain.dtos.petition;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePetitionRequest (
        @NotNull(message = "Message is required")
        @Size(max = 255, message = "Message must be less than 255 characters")
        String message,

        @NotNull(message = "Pet id is required")
        Long petId
) {
}
