package com.api.rescuemeapi.infrastructure.persistence.repositories;

import com.api.rescuemeapi.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
