package com.restmonkeys.rediscache;

public interface Converter<T> {
    public String from(T obj);

    public T to(String obj);
}
