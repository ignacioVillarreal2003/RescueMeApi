package com.api.rescuemeapi.domain.dtos.image;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteImageCommand implements Serializable {
    private UUID sagaId;
    private UUID referenceId;
    private Long imageId;
}
