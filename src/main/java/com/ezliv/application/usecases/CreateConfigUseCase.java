package com.ezliv.application.usecases;

import com.ezliv.application.gateways.ConfigGateway;


public class CreateConfigUseCase {
    private final ConfigGateway configGateway;

    public CreateConfigUseCase(ConfigGateway configGateway) {
        this.configGateway = configGateway;
    }

    public void execute(String parameter, String key, String value) {
        configGateway.saveConfig(parameter, key, value);
    }
}
