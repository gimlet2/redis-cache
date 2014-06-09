package com.restmonkeys.rediscache.converter;

public class LongConverter implements Converter<Long> {
    @Override
    public String from(Long obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    @Override
    public Long to(String obj) {
        return Long.parseLong(obj);
    }
}
