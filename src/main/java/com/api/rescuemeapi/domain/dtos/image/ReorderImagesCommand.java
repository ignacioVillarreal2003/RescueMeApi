package com.api.rescuemeapi.domain.dtos.image;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public class ReorderImagesCommand implements Serializable {
    private UUID sagaId;
    private UUID referenceId;
    private List<Long> orderedImageIds;
}
