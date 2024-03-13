package com.epam.epamgymdemo.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Boolean isActive;
}
