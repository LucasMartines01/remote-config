package com.ezliv.application.gateways;

import java.util.List;
import java.util.Map;

public interface ConfigGateway {
    public void saveConfig(List<String> customer, String parameter, String key, String value);

    public void updateConfig(List<String> customer, String parameter, String key, String value);

    public void deleteConfig(List<String> customer, String parameter, String key);

    public Map<String, Map<String, Object>> getAllConfigs(String customer);
}
