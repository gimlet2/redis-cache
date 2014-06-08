package com.restmonkeys.rediscache;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapConverter<T, K> implements Converter<Map<T, K>> {
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
            return new LinkedHashMap<T, K>(new ObjectMapper().readValue(obj, Map.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
