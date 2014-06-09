package com.restmonkeys.rediscache.converter;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapConverter<T, K> implements Converter<Map<T, K>> {
    private static Logger log = Logger.getLogger(MapConverter.class);

    @Override
    public String from(Map<T, K> obj) {
        if (obj == null) {
            return null;
        }
        return new JSONObject(obj).toString();
    }

    @Override
    public Map<T, K> to(String obj) {
        if (obj == null) {
            return null;
        }
        try {
            //noinspection unchecked
            return new LinkedHashMap<>(new ObjectMapper().readValue(obj, Map.class));
        } catch (IOException e) {
            log.warn("Error happened with parsing JSON to map", e);
        }
        return null;
    }
}
