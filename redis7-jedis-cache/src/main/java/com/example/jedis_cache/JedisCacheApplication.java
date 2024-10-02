package com.example.jedis_cache;

import com.example.jedis_cache.entity.UserEntity;
import com.example.jedis_cache.repository.UserRepository;
import lombok.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class JedisCacheApplication implements ApplicationRunner {
	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(JedisCacheApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		userRepository.save(UserEntity.builder().name("greg").email("greg@fastcampus.co.kr").build());
		userRepository.save(UserEntity.builder().name("tony").email("tony@fastcampus.co.kr").build());
		userRepository.save(UserEntity.builder().name("bob").email("bob@fastcampus.co.kr").build());
		userRepository.save(UserEntity.builder().name("ryan").email("ryan@fastcampus.co.kr").build());
	}
}
