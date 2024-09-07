package com.ezliv.application.usecases;

import com.ezliv.application.gateways.ConfigGateway;

import java.util.List;

public class CreateConfigUseCase {
    private final ConfigGateway configGateway;

    public CreateConfigUseCase(ConfigGateway configGateway) {
        this.configGateway = configGateway;
    }

    public void execute(List<String> customers, String parameter, String key, String value) {
        configGateway.saveConfig(customers, parameter, key, value);
    }
}
