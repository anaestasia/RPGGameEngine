package com.api.RPGGameEngine.item.dto;

import java.util.UUID;

import com.api.RPGGameEngine.common.enums.ItemRarity;
import com.api.RPGGameEngine.common.enums.ItemType;
import com.api.RPGGameEngine.common.enums.StatType;
import com.api.RPGGameEngine.item.Item;

public record ItemResponseDTO(
		UUID id,
        String name,
        String description,
        ItemType type,
        int bonus,
        StatType stat,
        ItemRarity rarity
) {
	public static ItemResponseDTO from(Item item) {
        return new ItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getType(),
                item.getBonus(),
                item.getStat(),
                item.getRarity()
        );
    }
}
