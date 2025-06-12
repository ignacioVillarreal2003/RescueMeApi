package com.api.rescuemeapi.infrastructure.persistence.repositories;

import com.api.rescuemeapi.domain.models.Pet;
import com.api.rescuemeapi.domain.models.Petition;
import com.api.rescuemeapi.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetitionRepository extends JpaRepository<Petition, Long>, JpaSpecificationExecutor<Petition> {

    @Query("select p from Petition p where p.requestingUser.id = ?1")
    List<Petition> findPetitionsByRequestingUser(long requestingUserId);

    @Query("select p from Petition p where p.requestedPet.id = ?1 and p.requestedPet.ownerUser.id = ?2")
    List<Petition> findPetitionsByPetAndOwnerUser(long petId, long ownerUserId);

    @Query("select p from Petition p where p.requestedPet.id = ?1 and p.requestingUser.id = ?2 ")
    Optional<Petition> findPetitionByPetAndRequestingUser(long petId, long requestingUserId);

    @Query("select p from Petition p where (p.requestingUser.id = ?1 or p.requestedPet.ownerUser.id = ?1) and p.status = 'APPROVED'")
    List<Petition> findApprovedPetitionsByUser(long userId);

    @Query("select p from Petition p where p.requestedPet.id = ?1")
    List<Petition> findPetitionsByPet(long petId);

    List<Petition> findAllByRequestingUser(User requestingUser);

    List<Petition> findAllByRequestedPet(Pet requestedPet);

    boolean existsByRequestedPetAndRequestingUser(Pet pet, User user);
}
