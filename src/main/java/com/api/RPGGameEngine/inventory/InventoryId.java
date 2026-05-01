package com.api.RPGGameEngine.inventory;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.api.RPGGameEngine.common.enums.SlotType;

public class InventoryId implements Serializable {
	private UUID hero;
    private UUID item;
    private SlotType slot;

    public InventoryId() {}

    public InventoryId(UUID hero, UUID item, SlotType slot) {
        this.hero = hero;
        this.item = item;
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryId that)) return false;
        return Objects.equals(hero, that.hero)
                && Objects.equals(item, that.item)
                && slot == that.slot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hero, item, slot);
    }
}
