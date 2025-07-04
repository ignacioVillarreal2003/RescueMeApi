package com.api.rescuemeapi.domain.dtos.pet;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class PetFilterRequest {
    private List<String> speciesList;
    private Integer age;
    private List<String> sizeList;
    private List<String> sexList;
    private Pageable pageable;
    private Boolean isOwned;
}
