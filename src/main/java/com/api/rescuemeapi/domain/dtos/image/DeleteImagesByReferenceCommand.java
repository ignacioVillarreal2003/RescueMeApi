package com.api.rescuemeapi.domain.dtos.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteImagesByReferenceCommand implements Serializable {
    private UUID sagaId;
    private UUID referenceId;
}
