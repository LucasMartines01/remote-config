package com.ezliv.infra.controller;

import com.ezliv.application.usecases.*;
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
    private final UpdateLocalConfigWithRemoteUseCase updateLocalConfigWithRemoteUseCase;

    public ConfigController(
            GetConfigsUseCase getConfigsUseCase,
            CreateConfigUseCase createConfigUseCase,
            UpdateConfigUseCase updateConfigUseCase,
            DeleteConfigUseCase deleteConfigUseCase,
            UpdateLocalConfigWithRemoteUseCase updateLocalConfigWithRemoteUseCase
    ) {
        this.getConfigsUseCase = getConfigsUseCase;
        this.createConfigUseCase = createConfigUseCase;
        this.updateConfigUseCase = updateConfigUseCase;
        this.deleteConfigUseCase = deleteConfigUseCase;
        this.updateLocalConfigWithRemoteUseCase = updateLocalConfigWithRemoteUseCase;
    }


    @GetMapping
    public ResponseEntity<Map<String, Map<String, Object>>> getConfigs() {
        return ResponseEntity.status(200).body(getConfigsUseCase.execute());
    }

    @PostMapping
    public ResponseEntity<Void> createConfig(@RequestBody @Valid ConfigDto configDto) {
        createConfigUseCase.execute(configDto.parameter(), configDto.key(), configDto.value());
        return ResponseEntity.status(201).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateConfig(@RequestBody @Valid ConfigDto configDto) {
        updateConfigUseCase.execute(configDto.parameter(), configDto.key(), configDto.value());
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteConfig(@RequestBody @Valid DeleteConfigDto configDto) {
        deleteConfigUseCase.execute(configDto.parameter(), configDto.key());
        return ResponseEntity.status(200).build();
    }

    @PutMapping()
    public ResponseEntity<Void> updateLocalConfigWithRemote() {
        updateLocalConfigWithRemoteUseCase.execute();
        return ResponseEntity.status(200).build();
    }
}
