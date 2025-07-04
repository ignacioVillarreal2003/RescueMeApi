package com.api.rescuemeapi.domain.dtos.user;

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
public class CompensateUserRegisterCommand implements Serializable {
    private UUID sagaId;
    private String reason;
    private Long userId;
}
