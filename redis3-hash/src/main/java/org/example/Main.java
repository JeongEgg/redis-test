package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1",6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // 하나의 데이터만 저장하는 경우
                jedis.hset("users:2:info", "name", "greg2");

                // 여러개의 데이터를 map 을 사용해 저장하는 경우
                var userInfo = new HashMap<String, String>();
                userInfo.put("email", "greg2@fastcampus.co.kr");
                userInfo.put("phone", "010-xxxx-xxxx");
                jedis.hset("users:2:info", userInfo);

                // 데이터 삭제
                jedis.hdel("users:2:info", "phone");

                // 데이터 가져오기
                System.out.println(jedis.hget("users:2:info", "email"));
                System.out.println("------------------------");
                // 모든 데이터 가져오기
                Map<String, String> user2Info = jedis.hgetAll("users:2:info");
                user2Info.forEach((k, v) -> System.out.printf("%s %s%n", k, v));

                // 증가
                jedis.hincrBy("users:2:info", "visits", 30);
            }
        }
    }
}