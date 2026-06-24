package com.api.RPGGameEngine.hero;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends JpaRepository<Hero, UUID> {
    boolean existsByName(String name);
}
