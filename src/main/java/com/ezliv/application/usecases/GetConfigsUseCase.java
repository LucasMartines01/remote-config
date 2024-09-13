package com.ezliv.application.usecases;

import com.ezliv.application.gateways.ConfigGateway;

import java.util.Map;

public class GetConfigsUseCase {
    private final ConfigGateway configGateway;

    public GetConfigsUseCase(ConfigGateway configGateway) {
        this.configGateway = configGateway;
    }

    public Map<String, Map<String, Object>> execute() {
        return configGateway.getAllConfigs();
    }
}
