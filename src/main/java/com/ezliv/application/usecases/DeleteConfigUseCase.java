package com.ezliv.application.usecases;

import com.ezliv.application.gateways.ConfigGateway;

import java.util.List;

public class DeleteConfigUseCase {
    private final ConfigGateway configGateway;

    public DeleteConfigUseCase(ConfigGateway configGateway) {
        this.configGateway = configGateway;
    }

    public void execute(List<String> customers, String parameter, String key) {
        configGateway.deleteConfig(customers, parameter, key);
    }
}
