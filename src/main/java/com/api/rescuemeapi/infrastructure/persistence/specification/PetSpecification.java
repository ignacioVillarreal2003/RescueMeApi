package com.api.rescuemeapi.infrastructure.persistence.specification;

import com.api.rescuemeapi.domain.enums.PetState;
import com.api.rescuemeapi.domain.models.Pet;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class PetSpecification {

    public static Specification<Pet> isOwned(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("owner_user_id"), userId);
    }

    public static Specification<Pet> isAvailable() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), PetState.AVAILABLE.toString());
    }

    public static Specification<Pet> hasSpeciesIn(List<String> speciesList) {
        return (root, query, criteriaBuilder) ->
                root.get("species").in(speciesList);
    }

    public static Specification<Pet> hasAge(int age) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("age"), 0, age);
    }

    public static Specification<Pet> hasSizeIn(List<String> sizeList) {
        return (root, query, criteriaBuilder) ->
                root.get("size").in(sizeList);
    }

    public static Specification<Pet> hasSexIn(List<String> sexList) {
        return (root, query, criteriaBuilder) ->
                root.get("sex").in(sexList);
    }
}
