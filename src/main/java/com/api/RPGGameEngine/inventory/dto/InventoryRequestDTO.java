package com.api.RPGGameEngine.inventory.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record InventoryRequestDTO(
		@NotNull (message = "L'id de l'item est obligatoire")
        UUID itemId
) {}
