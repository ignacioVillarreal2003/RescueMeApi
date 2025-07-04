package com.api.rescuemeapi.domain.dtos.petition;

import lombok.Data;

@Data
public class PetitionFilterRequest {
    private Boolean isRequestingByUser;
    private Long petId;
}
