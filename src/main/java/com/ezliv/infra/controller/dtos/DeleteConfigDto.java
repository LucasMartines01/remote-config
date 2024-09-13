package com.ezliv.infra.controller.dtos;

import jakarta.validation.constraints.NotBlank;

public record DeleteConfigDto(@NotBlank String parameter,
                              @NotBlank String key) {
}
