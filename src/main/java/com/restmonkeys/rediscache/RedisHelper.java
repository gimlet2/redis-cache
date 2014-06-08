package com.restmonkeys.rediscache;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.Properties;

public class RedisHelper implements Serializable {
    private static Logger log = Logger.getLogger(RedisHelper.class);

    private static final long serialVersionUID = 2511696851334487682L;
    public static final int DEFAULT_TIMEOUT = 86400;
    public static RedisHelper redisHelper = null;
    private JedisPool jedisPool = null;

    private RedisHelper() {

    }

    public static RedisHelper createRedisHelper() {
        if (redisHelper != null) {
            return redisHelper;
        }
        redisHelper = new RedisHelper();
        redisHelper.init();
        return redisHelper;
    }

    public static RedisHelper createRedisHelper(String host, String port) {
        redisHelper = new RedisHelper();
        redisHelper.init(host, port);
        return redisHelper;
    }

    private void init() {
        Properties properties = System.getProperties();
        if (properties.containsKey("redis.host")) {
            init(properties.getProperty("redis.host"), properties.getProperty("redis.port"));
        }
        log.warn("Redis caching disabled. To enable it add redis.host and redis.port to system properties.");
    }

    private void init(String host, String port) {
        jedisPool = new JedisPool(host, Integer.parseInt(port));
        log.debug("Redis caching enabled on " + host + ":" + port);
    }

    public boolean isValueExist(String name) {
        if (jedisPool == null) {
            return false;
        }
        Jedis jedis = jedisPool.getResource();
        Boolean result = jedis.exists(name);
        jedisPool.returnResource(jedis);
        return result;
    }

    public Cache getValue(String hash, String name) {
        if (jedisPool == null) {
            return new Cache(null, null, null);
        }
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(hash, name);
        jedisPool.returnResource(jedis);
        return new Cache(hash, name, result);
    }


    public void putValue(String name, String value) {
        if (jedisPool == null) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        jedis.set(name, value);
        jedisPool.returnResource(jedis);
    }

    public void putValueExpired(String hash, String name, String value, int timeout) {
        if (jedisPool == null || value == null) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        jedis.hset(hash, name, value);
        jedis.expire(hash, timeout);
        jedisPool.returnResource(jedis);
    }

    public void putValueExpired(String hash, String name, String value) {
        if (jedisPool == null) {
            return;
        }
        putValueExpired(hash, name, value, DEFAULT_TIMEOUT);
    }

    public class Cache {

        private String value;
        private String name;
        private String hash;
        private Converter converter = new Converter<String>() { // default converter for Strings values
            @Override
            public String from(String obj) {
                return obj;
            }

            @Override
            public String to(String obj) {
                //noinspection unchecked
                return obj;
            }
        };

        public Cache(String hash, String name, String value) {
            this.value = value;
            this.name = name;
            this.hash = hash;
        }

        public <T> T then(Eval<T> eval) {
            if (value != null) {
                //noinspection unchecked
                return (T) converter.to(value);
            } else {
                if (eval == null) {
                    return null;
                }
                T tmpValue = eval.get();
                if (tmpValue == null) {
                    return null;
                }
                //noinspection unchecked
                value = converter.from(tmpValue);
                putValueExpired(hash, name, value);
                //noinspection unchecked
                return tmpValue;
            }
        }

        public <T> Cache with(Converter<T> converter) {
            this.converter = converter;
            return this;
        }
    }

}

