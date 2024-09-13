package com.ezliv.application.gateways;

import java.util.List;
import java.util.Map;

public interface ConfigGateway {
    public void saveConfig(String parameter, String key, String value);

    public void updateConfig(String parameter, String key, String value);

    public void deleteConfig(String parameter, String key);

    public Map<String, Map<String, Object>> getAllConfigs();

    public void updateTemplateFromRemoteConfig();
}
