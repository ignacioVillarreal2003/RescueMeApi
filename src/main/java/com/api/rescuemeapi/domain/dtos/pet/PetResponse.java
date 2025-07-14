package com.api.rescuemeapi.domain.dtos.pet;

import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.api.rescuemeapi.domain.constants.PetSex;
import com.api.rescuemeapi.domain.constants.PetSize;
import com.api.rescuemeapi.domain.constants.PetSpecies;
import com.api.rescuemeapi.domain.constants.PetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private PetSpecies species;
    private Integer age;
    private PetSize size;
    private PetState state;
    private PetSex sex;
    private String breed;
    private String color;
    private Boolean isVaccinated;
    private Boolean isCastrated;
    private Boolean isDewormed;
    private String medicalNotes;
    private UserResponse ownerUser;
}