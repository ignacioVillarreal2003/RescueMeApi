package com.api.rescuemeapi.domain.dtos.image;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateImagesCommand implements Serializable {
    private UUID sagaId;
    private UUID referenceId;
    private List<ImagePayload> images;
}
