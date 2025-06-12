package com.api.rescuemeapi.domain.dtos.petition;

import com.api.rescuemeapi.domain.dtos.pet.PetResponse;
import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.api.rescuemeapi.domain.enums.PetitionStatus;
import lombok.Builder;

import java.io.Serializable;

@Builder
public class PetitionResponse implements Serializable {
    private Long id;
    private PetitionStatus status;
    private String message;
    private PetResponse requestedPet;
    private UserResponse requestingUser;
}
