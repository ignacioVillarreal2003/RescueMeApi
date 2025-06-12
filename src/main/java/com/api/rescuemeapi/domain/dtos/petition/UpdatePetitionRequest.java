package com.api.rescuemeapi.domain.dtos.petition;

import jakarta.validation.constraints.Size;

public record UpdatePetitionRequest(
        @Size(max = 255, message = "Message must be less than 255 characters")
        String message
) {
}
