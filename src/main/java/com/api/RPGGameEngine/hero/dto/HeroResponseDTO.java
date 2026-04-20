package com.api.RPGGameEngine.hero.dto;

import java.util.UUID;

import com.api.RPGGameEngine.common.enums.HeroRace;
import com.api.RPGGameEngine.common.enums.HeroRole;
import com.api.RPGGameEngine.hero.Hero;

public record HeroResponseDTO(
		UUID id,
		String name,
		int pv,
		int atq,
		int def,
		int speed,
		int exp,
		HeroRace race,
		HeroRole role
) {
	public static HeroResponseDTO from(Hero hero) {
		return new HeroResponseDTO(
				hero.getId(),
				hero.getName(),
				hero.getPv(),
				hero.getAtq(),
				hero.getDef(),
				hero.getSpeed(),
				hero.getExp(),
				hero.getRace(),
				hero.getRole()
		);
	}
}
