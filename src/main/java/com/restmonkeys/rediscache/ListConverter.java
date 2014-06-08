package com.restmonkeys.rediscache;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;

import java.io.IOException;
import java.util.List;

public class ListConverter<T> implements Converter<List<T>> {
    @Override
    public String from(List<T> obj) {
        if (obj == null) {
            return null;
        }
        return new JSONArray(obj).toString();
    }

    @Override
    public List<T> to(String obj) {
        if (obj == null) {
            return null;
        }
        try {
            //noinspection unchecked
            return new ObjectMapper().readValue(obj, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
