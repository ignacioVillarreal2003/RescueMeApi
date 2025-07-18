package com.api.rescuemeapi.domain.saga.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitiateUserRegistrationCommand implements Serializable {
        private UUID sagaId;
        private String email;
        private String password;
        private List<String> roles;
}
