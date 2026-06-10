package com.api.RPGGameEngine.item.dto;

import java.util.UUID;

import com.api.RPGGameEngine.common.enums.ItemType;
import com.api.RPGGameEngine.common.enums.StatType;

public record ItemResponseDTO(
		UUID id,
        String name,
        String description,
        ItemType type,
        int bonus,
        StatType stat
) {
	public static ItemResponseDTO from(com.api.RPGGameEngine.item.Item item) {
        return new ItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getType(),
                item.getBonus(),
                item.getStat()
        );
    }
}
