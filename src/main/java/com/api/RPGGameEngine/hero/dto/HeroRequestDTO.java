package com.api.RPGGameEngine.hero.dto;


import com.api.RPGGameEngine.common.enums.HeroRace;
import com.api.RPGGameEngine.common.enums.HeroRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HeroRequestDTO(
		@NotBlank(message = "Le nom est obligatoire")
        String name,

        @NotNull(message = "La race est obligatoire")
        HeroRace race,

        @NotNull(message = "Le rôle est obligatoire")
        HeroRole role
) {

}
