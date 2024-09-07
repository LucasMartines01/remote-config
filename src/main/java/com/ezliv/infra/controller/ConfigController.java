package com.ezliv.infra.controller;

import com.ezliv.application.usecases.CreateConfigUseCase;
import com.ezliv.application.usecases.GetConfigsUseCase;
import com.ezliv.infra.controller.dtos.CreateConfigDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/configs")
public class ConfigController {
    private final GetConfigsUseCase getConfigsUseCase;
    private final CreateConfigUseCase createConfigUseCase;

    public ConfigController(GetConfigsUseCase getConfigsUseCase, CreateConfigUseCase createConfigUseCase) {
        this.getConfigsUseCase = getConfigsUseCase;
        this.createConfigUseCase = createConfigUseCase;
    }


    @GetMapping("{customer}")
    public ResponseEntity<Map<String, Map<String, Object>>> getConfigs(@PathVariable String customer) {
        return ResponseEntity.status(200).body(getConfigsUseCase.execute(customer));
    }

    @PostMapping
    public ResponseEntity<Void> createConfig(@RequestBody @Valid CreateConfigDto createConfigDto) {
        createConfigUseCase.execute(createConfigDto.customers(), createConfigDto.parameter(), createConfigDto.key(), createConfigDto.value());
        return ResponseEntity.status(201).build();
    }

}
