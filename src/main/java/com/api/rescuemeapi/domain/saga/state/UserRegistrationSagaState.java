package com.api.rescuemeapi.domain.saga.state;

import com.api.rescuemeapi.domain.saga.step.UserRegistrationSagaStep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.UUID;

@RedisHash(value = "user_registration_saga_rescue_me_api", timeToLive = 7200)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationSagaState {

    @Id
    private UUID sagaId;

    private String firstName;

    private String lastName;

    private String phone;

    private String address;

    private Boolean success;

    private Integer status;

    private String errorMessage;

    private String email;

    private String token;

    private String refreshToken;

    private UserRegistrationSagaStep step;

    private Instant createdAt;

    private Instant updatedAt;

    public UserRegistrationSagaState(UUID sagaId, String firstName, String lastName, String phone, String address) {
        this.sagaId = sagaId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.step = UserRegistrationSagaStep.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void markStep(UserRegistrationSagaStep newStep) {
        this.step = newStep;
        this.updatedAt = Instant.now();
    }
}
