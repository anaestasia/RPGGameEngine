package com.api.RPGGameEngine.inventory.dto;

import java.util.UUID;

import com.api.RPGGameEngine.common.enums.ItemType;
import com.api.RPGGameEngine.common.enums.SlotType;
import com.api.RPGGameEngine.common.enums.StatType;
import com.api.RPGGameEngine.inventory.Inventory;

public record InventoryResponseDTO(
		UUID heroId,
        UUID itemId,
        String itemName,
        String itemDescription,
        ItemType itemType,
        int itemBonus,
        StatType itemStat,
        SlotType slot,
        boolean equipped
) {
	public static InventoryResponseDTO from(Inventory inventory) {
		return new InventoryResponseDTO(
				inventory.getHero().getId(),
				inventory.getItem().getId(),
				inventory.getItem().getName(),
				inventory.getItem().getDescription(),
				inventory.getItem().getType(),
				inventory.getItem().getBonus(),
				inventory.getItem().getStat(),
				inventory.getSlot(),
				inventory.isEquipped()
		);
	
	}
}
