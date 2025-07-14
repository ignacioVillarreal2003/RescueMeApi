package com.api.rescuemeapi.infrastructure.persistence.repositories;

import com.api.rescuemeapi.domain.models.UserRegisterSaga;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRegisterSagaRepository extends CrudRepository<UserRegisterSaga, UUID> {
    Optional<UserRegisterSaga> getUserRegisterSagaBySagaId(UUID sagaId);
}
