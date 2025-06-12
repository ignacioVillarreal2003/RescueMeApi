package com.api.rescuemeapi.domain.dtos.user;

import com.api.rescuemeapi.domain.enums.Role;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
public class RegisterUserCommand implements Serializable {
        private UUID sagaId;
        private String email;
        private String password;
        private List<Role> roles;
}
