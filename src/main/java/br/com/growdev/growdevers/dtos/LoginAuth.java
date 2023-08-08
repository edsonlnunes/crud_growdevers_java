package br.com.growdev.growdevers.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginAuth(
        @NotBlank
        String email,
        @NotBlank
        String password
) { }
