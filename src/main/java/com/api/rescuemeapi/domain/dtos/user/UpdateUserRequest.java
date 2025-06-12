package com.api.rescuemeapi.domain.dtos.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest (
        @Size(max = 64, message = "First name must be less than 64 characters")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "First name must only contain letters")
        String firstName,

        @Size(max = 64, message = "Last name must be less than 64 characters")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "Last name must only contain letters")
        String lastName,

        @Size(min = 9, max = 9, message = "Phone must be 9 characters")
        @Pattern(regexp = "^\\d{9}$", message = "Phone must contain exactly 9 digits")
        String phone,

        @Size(max = 255, message = "Address must be less than 255 characters")
        String address
) {

}
