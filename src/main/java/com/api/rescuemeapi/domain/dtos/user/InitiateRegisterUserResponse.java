package com.api.rescuemeapi.domain.dtos.user;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public class InitiateRegisterUserResponse implements Serializable {
        private UUID sagaId;
}
