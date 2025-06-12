package com.api.rescuemeapi.domain.dtos.image;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class DeleteImageRequest implements Serializable {
    private UUID referenceId;
    private Long imageId;
}