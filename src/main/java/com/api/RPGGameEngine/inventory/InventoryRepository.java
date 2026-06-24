package com.api.RPGGameEngine.inventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.RPGGameEngine.common.enums.SlotType;
import com.api.RPGGameEngine.hero.Hero;
import com.api.RPGGameEngine.item.Item;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
	
	/**
     * FetchType.LAZY charge Inventory sans charger Item.
     * Puis quand InventoryResponseDTO.from() appelle inventory.getItem().getName()
     * Hibernate fait une requête pour chaque item accédé => beaucoup de requêtes => pas optimisé
     * FetchType.EAGER chargerait toujours les relations même quand on en a pas besoin => pas optimisé
     * Solution : JOIN FETCH pour éviter le N+1
     */
	
	// Récupère tout l'inventaire d'un héros
    @Query("SELECT hi FROM Inventory hi JOIN FETCH hi.item WHERE hi.hero = :hero")
    List<Inventory> findByHeroWithItem(@Param("hero") Hero hero);

    // Récupère uniquement les items équipés ou non équipés
    @Query("SELECT hi FROM Inventory hi JOIN FETCH hi.item WHERE hi.hero = :hero AND hi.equipped = :equipped")
    List<Inventory> findByHeroAndEquippedWithItem(@Param("hero") Hero hero, @Param("equipped") boolean equipped);

    // Vérifie si un item est déjà dans l'inventaire du héros
    boolean existsByHeroAndItem(Hero hero, Item item);

    // Récupère un Inventory spécifique par hero + item
    Optional<Inventory> findByHeroAndItem(Hero hero, Item item);

    // Récupère un Inventory spécifique par hero + item + slot
    Optional<Inventory> findByHeroAndItemAndSlot(Hero hero, Item item, SlotType slot);

    // Compte les items dans un slot donné pour un héros
    long countByHeroAndSlot(Hero hero, SlotType slot);

    // Vérifie si un slot est occupé par un item équipé
    boolean existsByHeroAndSlotAndEquipped(Hero hero, SlotType slot, boolean equipped);

    // Récupère l'item équipé dans un slot donné
    Optional<Inventory> findByHeroAndSlotAndEquipped(Hero hero, SlotType slot, boolean equipped);
}
