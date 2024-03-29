package com.epam.epamgymdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordTokenDto {

    private String username;

    private String password;

    private String token;
}