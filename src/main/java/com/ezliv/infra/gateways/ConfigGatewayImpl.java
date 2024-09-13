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
    private final List<Customer> customers;

    public ConfigGatewayImpl(ConfigRepository configRepository, FirebaseRepository firebaseRepository, List<Customer> customers) {
        this.configRepository = configRepository;
        this.firebaseRepository = firebaseRepository;
        this.customers = customers;
    }

    @Override
    public void saveConfig(List<String> customer, String parameter, String key, String value) {
        for (String c : getCustomersToBeSaved(customer)) {
            configRepository.saveConfig(c, parameter, key, value).thenAcceptAsync(aVoid -> firebaseRepository.publishTemplate(c)).exceptionally(
                    throwable -> {
                        if (throwable.getCause() instanceof KeyAlreadyExists) {
                            throw new KeyAlreadyExists(throwable.getLocalizedMessage());
                        }
                        throw new ServerError(throwable.getLocalizedMessage(), throwable);
                    }
            ).join();
        }

    }

    @Override
    public void updateConfig(List<String> customer, String parameter, String key, String value) {
        for (String c : getCustomersToBeSaved(customer)) {
            configRepository.updateConfig(c, parameter, key, value).thenAcceptAsync(aVoid -> firebaseRepository.publishTemplate(c)).exceptionally(throwable -> {
                if (throwable.getCause() instanceof KeyNotExists) {
                    throw new KeyNotExists(throwable.getLocalizedMessage());
                }
                throw new ServerError(throwable.getLocalizedMessage(), throwable);
            }).join();
        }
    }

    @Override
    public void deleteConfig(List<String> customer, String parameter, String key) {
        for (String c : getCustomersToBeSaved(customer)) {
            configRepository.deleteConfig(c, parameter, key).thenAcceptAsync(aVoid -> firebaseRepository.publishTemplate(c)).exceptionally(throwable -> {
                if (throwable.getCause() instanceof KeyNotExists) {
                    throw new KeyNotExists(throwable.getLocalizedMessage());
                }
                throw new ServerError(throwable.getLocalizedMessage(), throwable);
            }).join();
        }
    }

    @Override
    public Map<String, Map<String, Object>> getAllConfigs(String customer) {
        return configRepository.getAllConfigs(customer).exceptionally(
                throwable -> {
                    throw new ServerError(throwable.getLocalizedMessage(), throwable);
                }
        ).join();
    }

    @Override
    public void updateTemplateFromRemoteConfig(String customer) {
        firebaseRepository.updateLocalParametersWithRemoteRepository(customer).thenAcceptAsync(aVoid -> firebaseRepository.publishTemplate(customer)).exceptionally(
                throwable -> {
                    throw new ServerError(throwable.getLocalizedMessage(), throwable);
                }
        ).join();
    }

    public List<String> getCustomersToBeSaved(List<String> customers) {
        List<String> customersToSave = new ArrayList<>();

        if (customers.contains("all")) {
            for (Customer c : this.customers) {
                customersToSave.add(c.getName());
            }
        } else {
            customersToSave.addAll(customers);
        }
        return customersToSave;
    }
}
