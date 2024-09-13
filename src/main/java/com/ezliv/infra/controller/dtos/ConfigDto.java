package com.ezliv.infra.controller.dtos;

import jakarta.validation.constraints.NotBlank;

public record ConfigDto(@NotBlank String parameter, @NotBlank String key, @NotBlank String value) {
}
