package com.ezliv.infra.controller;

import com.ezliv.application.usecases.CreateConfigUseCase;
import com.ezliv.application.usecases.DeleteConfigUseCase;
import com.ezliv.application.usecases.GetConfigsUseCase;
import com.ezliv.application.usecases.UpdateConfigUseCase;
import com.ezliv.infra.controller.dtos.ConfigDto;
import com.ezliv.infra.controller.dtos.DeleteConfigDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/configs")
public class ConfigController {
    private final GetConfigsUseCase getConfigsUseCase;
    private final CreateConfigUseCase createConfigUseCase;
    private final UpdateConfigUseCase updateConfigUseCase;
    private final DeleteConfigUseCase deleteConfigUseCase;

    public ConfigController(
            GetConfigsUseCase getConfigsUseCase,
            CreateConfigUseCase createConfigUseCase,
            UpdateConfigUseCase updateConfigUseCase,
            DeleteConfigUseCase deleteConfigUseCase
    ) {
        this.getConfigsUseCase = getConfigsUseCase;
        this.createConfigUseCase = createConfigUseCase;
        this.updateConfigUseCase = updateConfigUseCase;
        this.deleteConfigUseCase = deleteConfigUseCase;
    }


    @GetMapping("{customer}")
    public ResponseEntity<Map<String, Map<String, Object>>> getConfigs(@PathVariable String customer) {
        return ResponseEntity.status(200).body(getConfigsUseCase.execute(customer));
    }

    @PostMapping
    public ResponseEntity<Void> createConfig(@RequestBody @Valid ConfigDto configDto) {
        createConfigUseCase.execute(configDto.customers(), configDto.parameter(), configDto.key(), configDto.value());
        return ResponseEntity.status(201).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateConfig(@RequestBody @Valid ConfigDto configDto) {
        updateConfigUseCase.execute(configDto.customers(), configDto.parameter(), configDto.key(), configDto.value());
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteConfig(@RequestBody @Valid DeleteConfigDto configDto) {
        deleteConfigUseCase.execute(configDto.customers(), configDto.parameter(), configDto.key());
        return ResponseEntity.status(200).build();
    }

}
