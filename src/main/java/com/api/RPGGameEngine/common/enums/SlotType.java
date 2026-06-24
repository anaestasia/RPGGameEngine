package com.api.RPGGameEngine.common.enums;

import java.util.Set;

public enum SlotType {
	
	// On associe directement les types d'item autorisés pour chaque type de slot
	HEAD(1, Set.of(ItemType.HELMET)),
    CHEST(1, Set.of(ItemType.ARMOR)),
    LEFT_HAND(1, Set.of(ItemType.ONE_HAND_WEAPON, ItemType.TWO_HAND_WEAPON, ItemType.SHIELD)),
    RIGHT_HAND(1, Set.of(ItemType.ONE_HAND_WEAPON, ItemType.TWO_HAND_WEAPON, ItemType.SHIELD)),
    FINGER(2, Set.of(ItemType.RING)),
    FOOT(1, Set.of(ItemType.SHOE)),
    // 3 items max dans d l'inventaire contrairement à l'équipement
    INVENTORY(3, Set.of(ItemType.CONSUMABLE));

    private final int maxItems;
    private final Set<ItemType> acceptedTypes;

    SlotType(int maxItems, Set<ItemType> acceptedTypes) {
        this.maxItems = maxItems;
        this.acceptedTypes = acceptedTypes;
    }

    public int getMaxItems() {
        return maxItems;
    }

    // Permet de valider qu'un item peut aller dans le slot
    public boolean accepts(ItemType itemType) {
        return acceptedTypes.contains(itemType);
    }
}
