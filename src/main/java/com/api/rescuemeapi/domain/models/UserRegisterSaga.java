package com.api.rescuemeapi.domain.models;

import com.api.rescuemeapi.domain.constants.RegisterSagaStep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.UUID;

@RedisHash(value = "user_register_saga", timeToLive = 7200)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegisterSaga {

    @Id
    private UUID sagaId;

    private String firstName;

    private String lastName;

    private String phone;

    private String address;

    private Boolean success;

    private String errorMessage;

    private String email;

    private String token;

    private String refreshToken;

    private RegisterSagaStep step;

    private Instant createdAt;

    private Instant updatedAt;

    public UserRegisterSaga(UUID sagaId, String firstName, String lastName, String phone, String address) {
        this.sagaId = sagaId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.step = RegisterSagaStep.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void markStep(RegisterSagaStep newStep) {
        this.step = newStep;
        this.updatedAt = Instant.now();
    }
}
