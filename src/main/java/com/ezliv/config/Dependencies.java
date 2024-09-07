package com.ezliv.config;

import com.ezliv.application.gateways.ConfigGateway;
import com.ezliv.application.usecases.CreateConfigUseCase;
import com.ezliv.application.usecases.GetConfigsUseCase;
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
    ConfigGateway configGateway(ConfigRepository configRepository, FirebaseRepository firebaseRepository, List<Customer> customers) {
        return new ConfigGatewayImpl(configRepository, firebaseRepository, customers);
    }
}
