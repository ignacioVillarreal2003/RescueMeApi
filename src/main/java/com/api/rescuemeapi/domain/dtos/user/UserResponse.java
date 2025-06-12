package com.api.rescuemeapi.domain.dtos.user;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class UserResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
