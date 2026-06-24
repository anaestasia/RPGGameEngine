package com.api.RPGGameEngine.hero.dto;

import jakarta.validation.constraints.Min;

public record XpRequestDTO(
	    @Min(value = 1, message = "L'XP gagnée doit être d'au moins 1")
	    int amount
	) {}
