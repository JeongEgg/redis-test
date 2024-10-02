package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.Tuple;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1",6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // Sorted Set
                // 데이터 넣기
                var scores = new HashMap<String, Double>();
                scores.put("users1", 100.0);
                scores.put("users2", 30.0);
                scores.put("users3", 50.0);
                scores.put("users4", 80.0);
                scores.put("users5", 15.0);
                jedis.zadd("game2:scores", scores);

                // 데이터 출력(내림차순)
                List<String> zrange = jedis.zrange("game2:scores",0,Long.MAX_VALUE);
                zrange.forEach(System.out::println);
                System.out.println("-------------------------------");

                // 범위 설정에 의한 데이터 출력
                List<Tuple> tuples = jedis.zrangeWithScores("game2:scores",0,Long.MAX_VALUE);
                tuples.forEach(i -> System.out.println("%s %s".formatted(i.getElement(),i.getScore())));
                System.out.println("-------------------------------");

                // 갯수
                System.out.println(jedis.zcard("game2:scores"));
                System.out.println("-------------------------------");
                // 데이터에 값 증가시키기
                jedis.zincrby("game2:scores", 100.0, "users5");

                List<Tuple> tuples2 = jedis.zrangeWithScores("game2:scores",0,Long.MAX_VALUE);
                tuples2.forEach(i -> System.out.println("%s %s".formatted(i.getElement(),i.getScore())));

            }
        }
    }
}