package com.api.rescuemeapi.domain.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterSagaResponse implements Serializable {
    private boolean success;
    private UserResponse user;
    private String errorMessage;
    private String token;
    private String refreshToken;
}
