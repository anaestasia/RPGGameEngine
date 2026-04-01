package com.api.RPGGameEngine.inventory;

import com.api.RPGGameEngine.common.enums.SlotType;
import com.api.RPGGameEngine.hero.Hero;
import com.api.RPGGameEngine.item.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hero_item")
@IdClass(HeroItemId.class) // Mieux géré que @EmbeddedId car contient des relations JPA
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeroItem {
	@Id
    @ManyToOne(fetch = FetchType.LAZY) // LAZY = données chargées que si demandées
    @JoinColumn(name = "hero_id", nullable = false)
    private Hero hero;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "slot", nullable = false)
    private SlotType slot;

    @Column(name = "equipped", nullable = false)
    private boolean equipped;
}
