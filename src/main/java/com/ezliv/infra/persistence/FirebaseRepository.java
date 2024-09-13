package com.ezliv.infra.persistence;

import com.ezliv.domain.exceptions.ServerError;
import com.ezliv.infra.gateways.ConfigMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.Template;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Repository
public class FirebaseRepository {
    private final FirebaseApp firebaseApp;
    private final ConfigMapper configMapper;
    private final Gson gson;
    @Value("${defaults.path}")
    private String jsonPath;
    private static final String JSON_SUFFIX = ".json";
    @Value("${customer}")
    private String customer;

    public FirebaseRepository(FirebaseApp firebaseApp, ConfigMapper configMapper, Gson gson) {
        this.firebaseApp = firebaseApp;
        this.configMapper = configMapper;
        this.gson = gson;
    }

    @Async
    public void publishTemplate() {
        CompletableFuture.runAsync(() -> {
            try {
                FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance(firebaseApp);
                Template template = getTemplate();
                remoteConfig.forcePublishTemplate(template);
            } catch (Exception e) {
                throw new ServerError("Error while publishing template: ", e);
            }
        });
    }

    private Template getTemplate() {
        try (FileReader reader = new FileReader(jsonPath + customer + JSON_SUFFIX)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            return configMapper.jsonToTemplate(jsonObject);
        } catch (IOException e) {
            throw new ServerError("Error while reading configurations: ", e);
        }
    }

    @Async
    public CompletableFuture<Void> updateLocalParametersWithRemoteRepository() {
        return CompletableFuture.runAsync(() -> {
            try {
                FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance(firebaseApp);
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("parameters", remoteConfig.getTemplate().getParameters());
                try (FileWriter writer = new FileWriter(jsonPath + customer + JSON_SUFFIX)) {
                    gson.toJson(parameters, writer);
                } catch (IOException e) {
                    throw new ServerError("Error while updating local template with remote repository: ", e);
                }
            } catch (FirebaseRemoteConfigException e) {
                throw new ServerError("Error while updating local template with remote repository: ", e);
            }
        });
    }
}
