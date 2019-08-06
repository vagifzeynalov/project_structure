package com.sample.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.MappedSuperclass;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@MappedSuperclass
public abstract class Model implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Model.class);

    @Override
    public String toString() {
        try {
            return JsonSerializationComponent.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // do nothing
        }
        return super.toString();
    }

    protected static <T> Optional<T> fromJson(final String jsonStr, Class<T> clazz) {
        try {
            if (jsonStr != null && jsonStr.trim().length() > 0) {
                return Optional.ofNullable(JsonSerializationComponent.getObjectMapper().readValue(jsonStr, clazz));
            }
        } catch (IOException e) {
            LOGGER.trace("JSON ERROR", e);
        }
        return Optional.empty();
    }
}
