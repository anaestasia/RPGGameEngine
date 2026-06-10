package com.api.RPGGameEngine.inventory;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.RPGGameEngine.common.enums.ItemType;
import com.api.RPGGameEngine.common.enums.SlotType;
import com.api.RPGGameEngine.common.exceptions.ResourceNotFoundException;
import com.api.RPGGameEngine.hero.Hero;
import com.api.RPGGameEngine.hero.HeroRepository;
import com.api.RPGGameEngine.inventory.dto.InventoryResponseDTO;
import com.api.RPGGameEngine.inventory.dto.InventoryRequestDTO;
import com.api.RPGGameEngine.item.Item;
import com.api.RPGGameEngine.item.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
	
	private final InventoryRepository inventoryRepository;
	private final HeroRepository heroRepository;
	private final ItemRepository itemRepository;
	
	// ────────────────────────────────────────────────
    // GET - Inventaire / Equipement
    // ────────────────────────────────────────────────

    public List<InventoryResponseDTO> getInventory(UUID heroId) {
    	
        Hero hero = findHero(heroId);
        
        return inventoryRepository.findByHeroWithItem(hero)
                .stream()
                .map(InventoryResponseDTO::from)
                .toList();
    }

    public List<InventoryResponseDTO> getEquipment(UUID heroId) {

    	Hero hero = findHero(heroId);
    	
        return inventoryRepository.findByHeroAndEquippedWithItem(hero, true)
                .stream()
                .map(InventoryResponseDTO::from)
                .toList();
    }

    // ────────────────────────────────────────────────
    // POST — Ajout à l'inventaire
    // ────────────────────────────────────────────────
    public InventoryResponseDTO addToInventory(UUID heroId, InventoryRequestDTO dto) {
    	

    	Hero hero = findHero(heroId);
        Item item = findItem(dto.itemId());


        // Vérifie que l'item n'est pas déjà dans l'inventaire
        if (inventoryRepository.existsByHeroAndItem(hero, item)) {
            throw new IllegalArgumentException(
                    "L'item '" + item.getName() + "' est déjà dans l'inventaire du personnage");
        }
        
        Inventory inventory = Inventory.builder()
                .hero(hero)
                .item(item)
                .slot(SlotType.INVENTORY)
                .equipped(false)
                .build();

        return InventoryResponseDTO.from(inventoryRepository.save(inventory));
    }
    
    // ────────────────────────────────────────────────
    // PUT — Équiper
    // ────────────────────────────────────────────────

    @Transactional // Indispensable car 2 opérations en base, annule tout si l'une échoue
    public InventoryResponseDTO equip(UUID heroId, InventoryRequestDTO dto) {
        Hero hero = findHero(heroId);
        Item item = findItem(dto.itemId());

        // Vérifie que l'item est dans l'inventaire et non équipé
        Inventory inventory = inventoryRepository.findByHeroAndItem(hero, item)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "L'item '" + item.getName() + "' n'est pas dans l'inventaire du héros"));

        if (inventory.isEquipped()) {
            throw new IllegalStateException(
                    "L'item '" + item.getName() + "' est déjà équipé");
        }

        // Vérifie si ce n'est pas un consommable
        if (item.getType() == ItemType.CONSUMABLE) {
            throw new IllegalArgumentException(
                    "Les consommables ne peuvent pas être équipés");
        }
        
        // Détermine le slot automatiquement
        SlotType targetSlot = resolveSlot(hero, item);

        // Vérifie que le slot n'est pas plein
        long count = inventoryRepository.countByHeroAndSlot(hero, targetSlot);
        if (count >= targetSlot.getMaxItems()) {
            throw new IllegalStateException(
                    "Le slot " + targetSlot + " est déjà occupé");
        }

        // Cas TWO_HAND — vérifie et bloque les deux mains (return right_hand uniquement car représente l'item équipé)
        if (item.getType() == ItemType.TWO_HAND_WEAPON) {
            return InventoryResponseDTO.from(equipTwoHanded(hero, item, inventory));
        }

        // Cas standard
        inventoryRepository.delete(inventory); // Supprime d'abord le slot sinon modifie la clé primaire (interdit par Hibernate)
        inventoryRepository.flush(); // force la suppression avant l'insertion

        Inventory newInventory = Inventory.builder()
                .hero(hero)
                .item(item)
                .slot(targetSlot)
                .equipped(true)
                .build();
        
        return InventoryResponseDTO.from(inventoryRepository.save(newInventory));
    }
    
    // ────────────────────────────────────────────────
    // PUT — Déséquiper
    // ────────────────────────────────────────────────

    @Transactional 
    public InventoryResponseDTO unequip(UUID heroId, InventoryRequestDTO dto) {
        Hero hero = findHero(heroId);
        Item item = findItem(dto.itemId());

        Inventory inventory = inventoryRepository.findByHeroAndItem(hero, item)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "L'item '" + item.getName() + "' n'est pas dans l'inventaire du héros"));

        if (!inventory.isEquipped()) {
            throw new IllegalStateException(
                    "L'item '" + item.getName() + "' n'est pas équipé");
        }

        // Cas TWO_HAND — libère les deux mains
        if (item.getType() == ItemType.TWO_HAND_WEAPON) {
            unequipTwoHanded(hero, item);
            inventory.setSlot(SlotType.INVENTORY);
            inventory.setEquipped(false);
            return InventoryResponseDTO.from(inventoryRepository.save(inventory));
        }

        // Cas standard
        inventoryRepository.delete(inventory); // Supprime d'abord le slot sinon modifie la clé primaire (interdit par Hibernate)
        inventoryRepository.flush(); // force la suppression avant l'insertion
        
        Inventory newInventory = Inventory.builder()
                .hero(hero)
                .item(item)
                .slot(SlotType.INVENTORY)
                .equipped(false)
                .build();
        
        return InventoryResponseDTO.from(inventoryRepository.save(newInventory));
    }

    // ────────────────────────────────────────────────
    // DELETE — Retirer de l'inventaire
    // ────────────────────────────────────────────────

    public void removeFromInventory(UUID heroId, UUID itemId) {
        Hero hero = findHero(heroId);
        Item item = findItem(itemId);

        Inventory inventory = inventoryRepository.findByHeroAndItem(hero, item)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "L'item '" + item.getName() + "' n'est pas dans l'inventaire du héros"));

        if (inventory.isEquipped()) {
            throw new IllegalStateException(
                    "Impossible de retirer un item équipé — déséquipez-le d'abord");
        }

        inventoryRepository.delete(inventory);
    }
    
    // ────────────────────────────────────────────────
    // Méthodes privées
    // ────────────────────────────────────────────────
    
    private Hero findHero(UUID heroId) {
        return heroRepository.findById(heroId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Personnage introuvable avec l'id : " + heroId));
    }

    private Item findItem(UUID uuid) {
        return itemRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item introuvable avec l'id : " + uuid));
    }
    
    /**
     * Détermine automatiquement le slot cible en fonction du type d'item.
     * Pour ONE_HAND_WEAPON : RIGHT_HAND en priorité, LEFT_HAND si occupé.
     * Pour SHIELD : LEFT_HAND en priorité, RIGHT_HAND si occupé.
     */
    private SlotType resolveSlot(Hero hero, Item item) {
        return switch (item.getType()) {
            case ONE_HAND_WEAPON -> {
                boolean rightOccupied = inventoryRepository
                        .existsByHeroAndSlotAndEquipped(hero, SlotType.RIGHT_HAND, true);
                yield rightOccupied ? SlotType.LEFT_HAND : SlotType.RIGHT_HAND;
            }
            case SHIELD -> {
                boolean leftOccupied = inventoryRepository
                        .existsByHeroAndSlotAndEquipped(hero, SlotType.LEFT_HAND, true);
                yield leftOccupied ? SlotType.RIGHT_HAND : SlotType.LEFT_HAND;
            }
            default -> SlotType.getDefaultSlot(item.getType());
        };
    }

    /**
     * Équipe une arme à deux mains — occupe RIGHT_HAND et LEFT_HAND.
     * Crée une seconde entrée Inventory pour la main gauche.
     * @return 
     */
    private Inventory equipTwoHanded(Hero hero, Item item, Inventory inventory) {
        // Vérifie que les deux mains sont libres
        boolean rightOccupied = inventoryRepository
                .existsByHeroAndSlotAndEquipped(hero, SlotType.RIGHT_HAND, true);
        boolean leftOccupied = inventoryRepository
                .existsByHeroAndSlotAndEquipped(hero, SlotType.LEFT_HAND, true);

        if (rightOccupied || leftOccupied) {
            throw new IllegalStateException(
                    "Les deux mains doivent être libres pour équiper une arme à deux mains");
        }

        // Supprime l'entrée existante (sinon modifie la clé primaire => interdit par hibernate)
      
        inventoryRepository.delete(inventory);
        inventoryRepository.flush();
        
        
        // Main droite
        Inventory rightHand = Inventory.builder()
        		.hero(hero)
        		.item(item)
        		.slot(SlotType.RIGHT_HAND)
        		.equipped(true)
        		.build();

        // Main gauche
        Inventory leftHand = Inventory.builder()
                .hero(hero)
                .item(item)
                .slot(SlotType.LEFT_HAND)
                .equipped(true)
                .build();
        inventoryRepository.save(leftHand);
        return inventoryRepository.save(rightHand); // Return que right_hand car représente l'item équipé
    }

    /**
     * Déséquipe une arme à deux mains — libère RIGHT_HAND et LEFT_HAND.
     * Supprime l'entrée de la main gauche.
     */
    private void unequipTwoHanded(Hero hero, Item item) {
    	inventoryRepository.findByHeroAndItemAndSlot(hero, item, SlotType.LEFT_HAND)
                .ifPresent(inventoryRepository::delete);
    }
}
    
