package com.api.rescuemeapi.domain.dtos.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagePayload implements Serializable {
    private String filename;
    private String base64Data;
    private Integer orderIndex;
}
