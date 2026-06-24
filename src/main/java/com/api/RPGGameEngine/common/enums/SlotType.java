package com.api.RPGGameEngine.common.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum SlotType {
	
	// On associe directement les types d'item autorisés pour chaque type de slot
	HEAD(1, Set.of(ItemType.HELMET)),
    CHEST(1, Set.of(ItemType.ARMOR)),
    LEFT_HAND(1, Set.of(ItemType.ONE_HAND_WEAPON, ItemType.TWO_HAND_WEAPON, ItemType.SHIELD)),
    RIGHT_HAND(1, Set.of(ItemType.ONE_HAND_WEAPON, ItemType.TWO_HAND_WEAPON)),
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
    
    // Permet de récupérer les type de slots compatibles avec le type de l'item (ONE_HAND_WEAPON → [LEFT_HAND, RIGHT_HAND])

    public static List<SlotType> findSlotsFor(ItemType itemType) {
        return Arrays.stream(values())
                .filter(slot -> slot.accepts(itemType))
                .toList();
    }
    
    // Permet de retourner le slot par défaut (ONE_HAND_WEAPON → RIGHT_HAND en priorité)
    public static SlotType getDefaultSlot(ItemType itemType) {
        return switch (itemType) {
            case HELMET          -> HEAD;
            case ARMOR           -> CHEST;
            case SHOE            -> FOOT;
            case RING            -> FINGER;
            case ONE_HAND_WEAPON -> RIGHT_HAND;
            case TWO_HAND_WEAPON -> RIGHT_HAND;
            case SHIELD          -> LEFT_HAND;
            case CONSUMABLE      -> INVENTORY;
        };
    }
}
