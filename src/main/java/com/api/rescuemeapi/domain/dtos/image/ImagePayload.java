package com.api.rescuemeapi.domain.dtos.image;

import java.io.Serializable;

import lombok.Builder;

@Builder
public class ImagePayload implements Serializable {
    private String filename;
    private String base64Data;
    private Integer orderIndex;
}
