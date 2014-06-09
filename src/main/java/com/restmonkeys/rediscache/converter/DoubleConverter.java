package com.restmonkeys.rediscache.converter;

public class DoubleConverter implements Converter<Double> {
    @Override
    public String from(Double obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    @Override
    public Double to(String obj) {
        return Double.parseDouble(obj);
    }
}
