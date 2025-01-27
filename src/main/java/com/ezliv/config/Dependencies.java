package com.ezliv.config;

import com.ezliv.application.gateways.ConfigGateway;
import com.ezliv.application.usecases.*;
import com.ezliv.domain.entities.Customer;
import com.ezliv.infra.gateways.ConfigGatewayImpl;
import com.ezliv.infra.persistence.ConfigRepository;
import com.ezliv.infra.persistence.FirebaseRepository;
import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Dependencies {

    @Bean
    Gson gson() {
        return new Gson();
    }

    @Bean
    GetConfigsUseCase getConfigsUseCase(ConfigGateway configGateway) {
        return new GetConfigsUseCase(configGateway);
    }

    @Bean
    CreateConfigUseCase createConfigUseCase(ConfigGateway configGateway) {
        return new CreateConfigUseCase(configGateway);
    }

    @Bean
    UpdateConfigUseCase updateConfigUseCase(ConfigGateway configGateway) {
        return new UpdateConfigUseCase(configGateway);
    }

    @Bean
    DeleteConfigUseCase deleteConfigUseCase(ConfigGateway configGateway) {
        return new DeleteConfigUseCase(configGateway);
    }

    @Bean
    UpdateLocalConfigWithRemoteUseCase updateLocalConfigWithRemoteUseCase(ConfigGateway configGateway) {
        return new UpdateLocalConfigWithRemoteUseCase(configGateway);
    }

    @Bean
    ConfigGateway configGateway(ConfigRepository configRepository, FirebaseRepository firebaseRepository) {
        return new ConfigGatewayImpl(configRepository, firebaseRepository);
    }
}
