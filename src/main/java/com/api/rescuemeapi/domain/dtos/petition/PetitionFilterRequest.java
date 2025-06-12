package com.api.rescuemeapi.domain.dtos.petition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetitionFilterRequest {
    private Boolean isRequestingByUser;
    private Long petId;
}
