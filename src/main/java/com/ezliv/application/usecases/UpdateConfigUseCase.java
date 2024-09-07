package com.ezliv.application.usecases;

import com.ezliv.application.gateways.ConfigGateway;

import java.util.List;

public class UpdateConfigUseCase {
    private final ConfigGateway configGateway;

    public UpdateConfigUseCase(ConfigGateway configGateway) {
        this.configGateway = configGateway;
    }

    public void execute(List<String> customers, String parameter, String key, String value) {
        configGateway.updateConfig(customers, parameter, key, value);
    }
}
