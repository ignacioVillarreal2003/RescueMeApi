package com.api.rescuemeapi.domain.dtos.pet;

import com.api.rescuemeapi.domain.dtos.user.UserResponse;
import com.api.rescuemeapi.domain.enums.PetSex;
import com.api.rescuemeapi.domain.enums.PetSize;
import com.api.rescuemeapi.domain.enums.PetSpecies;
import com.api.rescuemeapi.domain.enums.PetState;
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
    private long id;
    private String name;
    private String description;
    private PetSpecies species;
    private int age;
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