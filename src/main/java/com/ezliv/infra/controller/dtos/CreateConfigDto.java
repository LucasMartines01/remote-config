package com.ezliv.infra.controller.dtos;

import com.ezliv.domain.entities.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateConfigDto(@NotEmpty @NotNull List<String> customers, @NotBlank String parameter,
                              @NotBlank String key, @NotBlank String value) {
}
