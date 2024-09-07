package com.ezliv.infra.persistence;

import com.ezliv.domain.exceptions.ServerError;
import com.ezliv.infra.gateways.ConfigMapper;
import com.google.firebase.remoteconfig.Template;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Repository
public class ConfigRepository {
    private final Gson gson;
    private final ConfigMapper configMapper;

    @Value("${defaults.path}")
    private String jsonPath;
    private static final String JSON_SUFFIX = ".json";


    public ConfigRepository(Gson gson, ConfigMapper configMapper) {
        this.gson = gson;
        this.configMapper = configMapper;
    }

    @Async
    public CompletableFuture<Void> saveConfig(String customer, String parameter, String key, String value) {
        return CompletableFuture.runAsync(() -> {
            try (FileReader reader = new FileReader(jsonPath + customer + JSON_SUFFIX)) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                JsonObject parameters = jsonObject.getAsJsonObject("parameters");
                JsonObject targetParameter = parameters.getAsJsonObject(parameter);

                JsonObject defaultValue = targetParameter.getAsJsonObject("defaultValue");

                Type mapType = new TypeToken<Map<String, Object>>() {
                }.getType();
                Map<String, Object> configValues = gson.fromJson(defaultValue.get("value").getAsString(), mapType);

                List<String> keys = List.of(key.split("\\."));

                Map<String, Object> currentMap = configValues;
                for (int i = 0; i < keys.size() - 1; i++) {
                    String newKey = keys.get(i);
                    if (currentMap.containsKey(newKey) && currentMap.get(newKey) instanceof Map) {
                        currentMap = (Map<String, Object>) currentMap.get(newKey);
                    } else {
                        Map<String, Object> newMap = new HashMap<>();
                        currentMap.put(newKey, newMap);
                        currentMap = newMap;
                    }
                }

                String finalKey = keys.get(keys.size() - 1);
                if (currentMap.containsKey(finalKey)) {
                    throw new KeyAlreadyExistsException("The key '" + finalKey + "' already exists.");
                }
                currentMap.put(finalKey, value);

                defaultValue.addProperty("value", gson.toJson(configValues));

                try (FileWriter writer = new FileWriter(jsonPath + customer + JSON_SUFFIX)) {
                    gson.toJson(jsonObject, writer);
                }
            } catch (IOException e) {
                throw new ServerError("Error while saving configurations", e);
            }
        });
    }

    public CompletableFuture<Void> updateConfig(String customer, String parameter, String key, String value) {
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> deleteConfig(String customer, String parameter, String key) {
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Map<String, Map<String, Object>>> getAllConfigs(String customer) {
        return CompletableFuture.supplyAsync(() -> {
            try (FileReader reader = new FileReader(jsonPath + customer + JSON_SUFFIX)) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                return configMapper.jsonToMap(jsonObject);
            } catch (Exception e) {
                throw new ServerError("Error while reading configurations", e);
            }
        });
    }
}
