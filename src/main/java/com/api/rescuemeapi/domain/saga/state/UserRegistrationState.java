package com.api.rescuemeapi.domain.saga.state;

import com.api.rescuemeapi.domain.saga.step.UserRegistrationStep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.UUID;

@RedisHash(value = "user_registration_state_rescue_me_api", timeToLive = 7200)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationState {

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

    private UserRegistrationStep step;

    private Instant createdAt;

    private Instant updatedAt;

    public UserRegistrationState(UUID sagaId, String email, String firstName, String lastName, String phone, String address) {
        this.sagaId = sagaId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.step = UserRegistrationStep.STARTED;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void markStep(UserRegistrationStep newStep) {
        this.step = newStep;
        this.updatedAt = Instant.now();
    }
}
