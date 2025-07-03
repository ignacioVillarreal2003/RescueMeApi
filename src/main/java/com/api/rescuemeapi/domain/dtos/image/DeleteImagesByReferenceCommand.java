package com.api.rescuemeapi.domain.dtos.image;

import java.io.Serializable;
import java.util.UUID;

import lombok.Builder;

@Builder
public class DeleteImagesByReferenceCommand implements Serializable {
    private UUID sagaId;
    private UUID referenceId;
}
