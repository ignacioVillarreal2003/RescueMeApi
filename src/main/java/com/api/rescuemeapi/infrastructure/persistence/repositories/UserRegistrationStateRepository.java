package com.api.rescuemeapi.infrastructure.persistence.repositories;

import com.api.rescuemeapi.domain.saga.state.UserRegistrationState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRegistrationStateRepository extends CrudRepository<UserRegistrationState, UUID> {
}
