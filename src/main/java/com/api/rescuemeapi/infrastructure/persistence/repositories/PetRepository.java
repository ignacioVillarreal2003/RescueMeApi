package com.api.rescuemeapi.infrastructure.persistence.repositories;

import com.api.rescuemeapi.domain.models.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long>, JpaSpecificationExecutor<Pet> {

    @Query("select p from Pet p where p.ownerUser.id = ?1")
    List<Pet> findPetsByOwner(long userId);
}
