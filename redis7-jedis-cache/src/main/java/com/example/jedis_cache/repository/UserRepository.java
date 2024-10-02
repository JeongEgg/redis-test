package com.example.jedis_cache.repository;

import com.example.jedis_cache.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
