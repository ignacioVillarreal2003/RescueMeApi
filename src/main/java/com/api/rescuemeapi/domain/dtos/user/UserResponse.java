package com.api.rescuemeapi.domain.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
