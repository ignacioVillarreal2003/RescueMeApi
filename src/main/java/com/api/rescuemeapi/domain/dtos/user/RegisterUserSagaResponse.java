package com.api.rescuemeapi.domain.dtos.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterUserSagaResponse {
    boolean success;
    UserResponse user;
    String errorMessage;
    String token;
    String refreshToken;
}
