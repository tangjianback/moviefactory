package com.example.dao;

import redis.clients.jedis.Jedis;

public class Redis_instance {
    static Jedis jedis = null;
    static {
        Jedis jedis = new Jedis("localhost",6379);
    }
    public static Jedis get_redis(){
        return jedis;
    }
}
