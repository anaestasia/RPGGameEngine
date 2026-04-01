package com.api.RPGGameEngine.inventory;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.api.RPGGameEngine.common.enums.SlotType;

public class HeroItemId implements Serializable {
	private UUID hero;
    private UUID item;
    private SlotType slot;

    public HeroItemId() {}

    public HeroItemId(UUID hero, UUID item, SlotType slot) {
        this.hero = hero;
        this.item = item;
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeroItemId that)) return false;
        return Objects.equals(hero, that.hero)
                && Objects.equals(item, that.item)
                && slot == that.slot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hero, item, slot);
    }
}
