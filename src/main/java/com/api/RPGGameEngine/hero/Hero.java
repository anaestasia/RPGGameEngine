package com.api.RPGGameEngine.hero;

import java.util.UUID;

import com.api.RPGGameEngine.common.enums.HeroRace;
import com.api.RPGGameEngine.common.enums.HeroRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hero")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hero {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "pv", nullable = false)
    private int pv;

    @Column(name = "atq", nullable = false)
    private int atq;

    @Column(name = "def", nullable = false)
    private int def;

    @Column(name = "speed", nullable = false)
    private int speed;

    @Column(name = "exp", nullable = false)
    private int exp;

    @Enumerated(EnumType.STRING)
    @Column(name = "race", nullable = false)
    private HeroRace race;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private HeroRole role;
}
