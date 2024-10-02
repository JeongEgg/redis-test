package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1",6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // --------------------  LIST  ----------------------
                jedis.rpush("stack1","aaaa");
                jedis.rpush("stack1","bbbb");
                jedis.rpush("stack1","cccc");

                // lrange : 왼쪽에서부터 데이터 확인
                List<String> stack1 = jedis.lrange("stack1", 0, -1);
                stack1.forEach(System.out::println);

                System.out.println("----------------");
                jedis.rpush("queue2","zzzz");
                jedis.rpush("queue2","aaaa");
                jedis.rpush("queue2","cccc");

                // lpop : 왼쪽에서부터 데이터 하나씩 꺼내기
                System.out.println(jedis.lpop("queue2"));
                System.out.println(jedis.lpop("queue2"));
                System.out.println(jedis.lpop("queue2"));

                System.out.println("----------------");

                // 10초 동안
                List<String> blpop = jedis.blpop(10, "queue:blocking");
                if (blpop != null){
                    blpop.forEach(System.out::println);
                }
                // ------------------   SET   ------------------------
                // SET 은 중복값을 허용하지 않는다.

                // sadd : SET 데이터 저장
                jedis.sadd("users:500:follow","100","200","300");
                // srem : SET 데이터 삭제
                jedis.srem("users:500:follow","100");

                // smember : 전체 멤버 가져옴
                Set<String> smembers = jedis.smembers("users:500:follow");
                smembers.forEach(System.out::println); // 출력 : 200, 300

                // sismember : 특정 값 포함여부 확인
                System.out.println(jedis.sismember("users:500:follow", "200")); // true
                System.out.println(jedis.sismember("users:500:follow", "120")); // false

                // scard : 갯수 출력
                System.out.println(jedis.scard("users:500:follow"));

                // 2개의 SET 의 공통된 값을 출력
                Set<String> sinter =  jedis.sinter("users:500:follow", "users:100:follow");
                sinter.forEach(System.out::println);
            }
        }
    }
}