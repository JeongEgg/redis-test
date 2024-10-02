package com.example.cache.service;

import com.example.cache.domain.entity.RedisHashUser;
import com.example.cache.domain.entity.User;
import com.example.cache.domain.repository.RedisHashUserRepository;
import com.example.cache.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.example.cache.config.CacheConfig.CACHE1;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;
    private final RedisHashUserRepository redisHashUserRepository;

    public User getUser(final Long id) {
        /** 먼저 레디스에서 데이터를 조회한 후, 데이터가 없으면 mysql 에서 데이터를 가져오고
         *  레디스에 가져온 데이터를 넣는다.*/
        // 1. cache get
        var key = "users:%d".formatted(id);
//        var cachedUser = userRedisTemplate.opsForValue().get(key);
        var cachedUser = objectRedisTemplate.opsForValue().get(key);
        if (cachedUser != null){
//            return cachedUser;
            return (User) cachedUser;
        }

        // 2. else db -> cache set
        User user = userRepository.findById(id).orElseThrow();
//        userRedisTemplate.opsForValue().set(key,user);
        // 레디스에 저장 시간을 설정
        objectRedisTemplate.opsForValue().set(key,user, Duration.ofSeconds(30));

        return user;
    }

    public RedisHashUser getUser2(final Long id) {
        // redis 값이 있으면 리턴. 없으면 db 에서 찾음
        var cachedUser = redisHashUserRepository.findById(id).orElseGet(() -> {
            User user = userRepository.findById(id).orElseThrow();
            return redisHashUserRepository.save(RedisHashUser.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .email(user.getEmail())
                            .createdAt(user.getCreatedAt())
                            .updatedAt(user.getUpdatedAt())
                            .build());
        });
        return cachedUser;
    }

    @Cacheable(cacheNames = CACHE1, key = "'user:' + #id")
    public User getUser3(final Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
