package com.api.rescuemeapi.infrastructure.persistence.repositories;

import com.api.rescuemeapi.domain.models.Pet;
import com.api.rescuemeapi.domain.models.Petition;
import com.api.rescuemeapi.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetitionRepository extends JpaRepository<Petition, Long>, JpaSpecificationExecutor<Petition> {

    List<Petition> findAllByRequestingUser(User requestingUser);

    List<Petition> findAllByRequestedPet(Pet requestedPet);

    boolean existsByRequestedPetAndRequestingUser(Pet pet, User user);
}
