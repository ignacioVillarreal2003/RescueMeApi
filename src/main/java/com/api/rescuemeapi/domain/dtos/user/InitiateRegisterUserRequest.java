package com.api.rescuemeapi.domain.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InitiateRegisterUserRequest(

        @NotNull(message = "Email is required")
        @Email(message = "Email is invalid")
        @Size(max = 64, message = "Email must be less than 64 characters")
        String email,

        @NotNull(message = "Password is required")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        String password,

        @NotNull(message = "First name is required")
        @Size(max = 64, message = "First name must be less than 64 characters")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "First name must only contain letters")
        String firstName,

        @NotNull(message = "Last name is required")
        @Size(max = 64, message = "Last name must be less than 64 characters")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "Last name must only contain letters")
        String lastName,

        @NotNull(message = "Phone is required")
        @Size(min = 9, max = 9, message = "Phone must be 9 characters")
        @Pattern(regexp = "^\\d{9}$", message = "Phone must contain exactly 9 digits")
        String phone,

        @NotNull(message = "Address is required")
        @Size(max = 255, message = "Address must be less than 255 characters")
        String address
) {
    
}
