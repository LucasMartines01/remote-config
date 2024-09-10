package com.ezliv.infra.controller.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ConfigDto(@NotEmpty @NotNull List<String> customers, @NotBlank String parameter,
                        @NotBlank String key, @NotBlank String value) {
}
