package com.sample.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
final class JsonSerializationComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSerializationComponent.class);

    private static JsonSerializationComponent instance;

    private ObjectMapper objectMapper;

    public JsonSerializationComponent() {
        updateInstance();
    }

    static ObjectMapper getObjectMapper() {
        if (instance != null && instance.objectMapper != null) {
            LOGGER.trace("Return existent Object Mapper object");
            return instance.objectMapper;
        }
        LOGGER.trace("Create a new Object Mapper object");
        return new ObjectMapper();
    }

    @Autowired(required = false)
    public void setObjectMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private synchronized void updateInstance() {
        if (instance == null) {
            instance = this;
        }
    }
}
