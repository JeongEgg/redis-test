
package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1",6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // bitmap
                jedis.setbit("request-somepage-20230305", 100, true);
                jedis.setbit("request-somepage-20230305", 200, true);
                jedis.setbit("request-somepage-20230305", 300, true);

                System.out.println(jedis.getbit("request-somepage-20230305",100)); // true
                System.out.println(jedis.getbit("request-somepage-20230305",50)); // false
                System.out.println(jedis.bitcount("request-somepage-20230305")); // 3

                // bitmap vs set
                // redis-cli monitor 에서 메모리 사용량 비교
                Pipeline pipeline = jedis.pipelined();
                IntStream.rangeClosed(0, 100000).forEach(i -> {
                    pipeline.sadd("request-somepage-set-20230306", String.valueOf(i), "1");
                    pipeline.setbit("request-somepage-bit-20230306",i,true);

                    if (i==1000){
                        pipeline.sync();
                    }
                });
                pipeline.sync();
            }
        }
    }
}