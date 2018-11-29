package com.alpersemerci.tictactoe.service.config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Service class for configuration file operations.
 */
@Slf4j
public class ConfigService {

    private static final String CONFIG_PREFIX = "tictactoe.";
    private static final ConfigService instance = new ConfigService();
    private Properties properties;

    private ConfigService() {
        try {
            initProperties();
        } catch (IOException e) {
            log.error("[CONFIG_SERVICE] [INIT_ERROR={}]", e.getMessage(), e);
        }
    }

    public static ConfigService getInstance() {
        return instance;
    }

    /**
     * Transforms config.properties into java properties map.
     *
     * @throws IOException
     */
    private void initProperties() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "config.properties";
        properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));
    }

    /**
     * Returns config value for given key.
     *
     * @param key
     * @return
     */
    public String getConfigValue(String key) {
        if (properties != null) {
            return properties.getProperty(CONFIG_PREFIX + key);
        } else {
            return null;
        }
    }
}
