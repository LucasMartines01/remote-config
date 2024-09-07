package com.ezliv.infra.gateways;

import com.ezliv.application.gateways.ConfigGateway;
import com.ezliv.domain.entities.Customer;
import com.ezliv.domain.exceptions.KeyAlreadyExists;
import com.ezliv.domain.exceptions.ServerError;
import com.ezliv.infra.persistence.ConfigRepository;
import com.ezliv.infra.persistence.FirebaseRepository;

import javax.management.openmbean.KeyAlreadyExistsException;
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
        try {
            for (String c : getCustomersToBeSaved(customer)) {
                configRepository.saveConfig(c, parameter, key, value).thenAcceptAsync(aVoid -> firebaseRepository.publishTemplate(c));
            }
        } catch (Exception e) {
            if (e.getCause() instanceof KeyAlreadyExistsException) {
                throw new KeyAlreadyExists("Key already exists");
            }
            throw new ServerError("Error while saving configuration", e);
        }
    }

    @Override
    public void updateConfig(List<String> customer, String parameter, String key, String value) {

    }

    @Override
    public void deleteConfig(List<String> customer, String parameter, String key) {

    }

    @Override
    public Map<String, Map<String, Object>> getAllConfigs(String customer) {
        try {
            return configRepository.getAllConfigs(customer).join();
        } catch (Exception e) {
            throw new ServerError("Error while reading configurations", e);
        }
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
