package com.api.rescuemeapi.domain.dtos.image;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class CreateImageRequest implements Serializable {
    private UUID referenceId;
    private String imageName;
    private String imageBase64;
}
