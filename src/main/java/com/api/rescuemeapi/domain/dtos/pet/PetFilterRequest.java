package com.api.rescuemeapi.domain.dtos.pet;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
public class PetFilterRequest {
    private List<String> speciesList;
    private Integer age;
    private List<String> sizeList;
    private List<String> sexList;
    private Pageable pageable;
    private Boolean isOwned;
}
