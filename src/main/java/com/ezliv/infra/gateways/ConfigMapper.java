package com.ezliv.infra.gateways;

import com.ezliv.domain.exceptions.ServerError;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.Template;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigMapper {
    private final Gson gson;

    public ConfigMapper(Gson gson) {
        this.gson = gson;
    }

    public Map<String, Map<String, Object>> jsonToMap(JsonObject jsonObject) {
        Map<String, Map<String, Object>> map = new HashMap<>();

        JsonObject parameters = jsonObject.getAsJsonObject("parameters");

        for (int i = 0; i < parameters.size(); i++) {
            JsonObject parameter = parameters.getAsJsonObject(parameters.keySet().toArray()[i].toString());
            JsonObject defaultValue = parameter.getAsJsonObject("defaultValue");
            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();

            Map<String, Object> configValues = gson.fromJson(defaultValue.get("value").getAsString(), mapType);

            map.put(parameters.keySet().toArray()[i].toString(), configValues);
        }

        return map;
    }

    public Template jsonToTemplate(JsonObject jsonObject) {
        try {
            return Template.fromJSON(jsonObject.toString());
        } catch (FirebaseRemoteConfigException e) {
            throw new ServerError("Error while converting JSON to Template", e);
        }
    }
}
