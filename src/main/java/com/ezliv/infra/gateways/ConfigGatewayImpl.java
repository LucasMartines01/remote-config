package com.ezliv.infra.gateways;

import com.ezliv.application.gateways.ConfigGateway;
import com.ezliv.domain.entities.Customer;
import com.ezliv.domain.exceptions.KeyAlreadyExists;
import com.ezliv.domain.exceptions.KeyNotExists;
import com.ezliv.domain.exceptions.ServerError;
import com.ezliv.infra.persistence.ConfigRepository;
import com.ezliv.infra.persistence.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigGatewayImpl implements ConfigGateway {
    private final ConfigRepository configRepository;
    private final FirebaseRepository firebaseRepository;

    public ConfigGatewayImpl(ConfigRepository configRepository, FirebaseRepository firebaseRepository) {
        this.configRepository = configRepository;
        this.firebaseRepository = firebaseRepository;
    }

    @Override
    public void saveConfig(String parameter, String key, String value) {

        configRepository.saveConfig(parameter, key, value).thenAcceptAsync(aVoid -> firebaseRepository.publishTemplate()).exceptionally(
                throwable -> {
                    if (throwable.getCause() instanceof KeyAlreadyExists) {
                        throw new KeyAlreadyExists(throwable.getLocalizedMessage());
                    }
                    throw new ServerError(throwable.getLocalizedMessage(), throwable);
                }
        ).join();


    }

    @Override
    public void updateConfig(String parameter, String key, String value) {

        configRepository.updateConfig(parameter, key, value).thenAcceptAsync(aVoid -> firebaseRepository.publishTemplate()).exceptionally(throwable -> {
            if (throwable.getCause() instanceof KeyNotExists) {
                throw new KeyNotExists(throwable.getLocalizedMessage());
            }
            throw new ServerError(throwable.getLocalizedMessage(), throwable);
        }).join();
    }

    @Override
    public void deleteConfig(String parameter, String key) {

        configRepository.deleteConfig(parameter, key).thenAcceptAsync(aVoid -> firebaseRepository.publishTemplate()).exceptionally(throwable -> {
            if (throwable.getCause() instanceof KeyNotExists) {
                throw new KeyNotExists(throwable.getLocalizedMessage());
            }
            throw new ServerError(throwable.getLocalizedMessage(), throwable);
        }).join();

    }

    @Override
    public Map<String, Map<String, Object>> getAllConfigs() {
        return configRepository.getAllConfigs().exceptionally(
                throwable -> {
                    throw new ServerError(throwable.getLocalizedMessage(), throwable);
                }
        ).join();
    }

    @Override
    public void updateTemplateFromRemoteConfig() {
        firebaseRepository.updateLocalParametersWithRemoteRepository().thenAcceptAsync(aVoid -> firebaseRepository.publishTemplate()).exceptionally(
                throwable -> {
                    throw new ServerError(throwable.getLocalizedMessage(), throwable);
                }
        ).join();
    }
}
