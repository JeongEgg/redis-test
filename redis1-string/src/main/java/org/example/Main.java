package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        try (var jedisPool = new JedisPool("127.0.0.1",6379)){
            try (Jedis jedis = jedisPool.getResource()){
                jedis.set("users:300:email", "kim@fastcampus.co.kr");
                jedis.set("users:300:name", "kim 00");
                jedis.set("users:300:age", "100");

                var userEmail = jedis.get("users:300:email");
                System.out.println(userEmail);

                List<String> userInfo = jedis.mget("users:300:email", "users:300:name", "users:300:age");
                System.out.println("-----------------------------");
                userInfo.forEach(i -> System.out.println(i));

                System.out.println("-----------------------------");

                // 1 증가
                long counter = jedis.incr("counter");
                System.out.println(counter);

                // 10 증가
                long counter2 = jedis.incrBy("counter", 10L);
                System.out.println(counter2);

                System.out.println("-----------------------------");
                // redis 에 한꺼번에 데이터를 전송함.
                Pipeline pipeline = jedis.pipelined();
                pipeline.set("users:400:email", "greg@fastcampus.co.kr");
                pipeline.set("users:400:name", "greg");
                pipeline.set("users:400:age", "15");
                List<Object> objects = pipeline.syncAndReturnAll();
                objects.forEach(i -> System.out.println(i));
            }
        }
    }
}