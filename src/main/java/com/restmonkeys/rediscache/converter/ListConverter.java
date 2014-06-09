package com.restmonkeys.rediscache.converter;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;

import java.io.IOException;
import java.util.List;

public class ListConverter<T> implements Converter<List<T>> {

    private static Logger log = Logger.getLogger(ListConverter.class);

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
            log.warn("Error happened with parsing JSON to list", e);
        }
        return null;
    }
}
