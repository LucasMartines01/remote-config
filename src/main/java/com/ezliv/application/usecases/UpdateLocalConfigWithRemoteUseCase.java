package com.ezliv.application.usecases;

import com.ezliv.application.gateways.ConfigGateway;

public class UpdateLocalConfigWithRemoteUseCase {
    private final ConfigGateway configGateway;

    public UpdateLocalConfigWithRemoteUseCase(ConfigGateway configGateway) {
        this.configGateway = configGateway;
    }

    public void execute(String customer) {
        configGateway.updateTemplateFromRemoteConfig(customer);
    }
}
