package com.api.rescuemeapi.infrastructure.persistence.repositories;

import com.api.rescuemeapi.domain.saga.state.UserRegistrationSagaState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRegistrationSagaRepository extends CrudRepository<UserRegistrationSagaState, UUID> {
}
