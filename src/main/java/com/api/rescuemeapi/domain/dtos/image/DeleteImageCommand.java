package com.api.rescuemeapi.domain.dtos.image;

import java.io.Serializable;
import java.util.UUID;

import lombok.Builder;

@Builder
public class DeleteImageCommand implements Serializable {
    private UUID sagaId;
    private UUID referenceId;
    private Long imageId;
}
