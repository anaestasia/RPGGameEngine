package com.api.RPGGameEngine.item;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.RPGGameEngine.common.enums.ItemRarity;
import com.api.RPGGameEngine.common.enums.ItemType;
import com.api.RPGGameEngine.common.exceptions.ResourceNotFoundException;
import com.api.RPGGameEngine.item.dto.ItemRequestDTO;
import com.api.RPGGameEngine.item.dto.ItemResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
	
	
	private final ItemRepository itemRepository;
    
    // Liste tous les items, filtre par type et/ou par rareté
    public List<ItemResponseDTO> findByFilters(ItemType type, ItemRarity rarity) {
    	
    	List<Item> items;
    	
    	if (type != null && rarity != null) {
            items = itemRepository.findByTypeAndRarity(type, rarity);
        } else if (type != null) {
            items = itemRepository.findByType(type);
        } else if (rarity != null) {
            items = itemRepository.findByRarity(rarity);
        } else {
            items = itemRepository.findAll();
        }
    	
        return items.stream()
                .map(ItemResponseDTO::from)
                .toList();
    }

    // Récupère l'item par son ID
    public ItemResponseDTO findById(UUID id) {
    	
        return itemRepository.findById(id)
                .map(ItemResponseDTO::from)
                .orElseThrow(() -> new ResourceNotFoundException("Item introuvable avec l'id : " + id));
    }

    // Créer l'item
    public ItemResponseDTO create(ItemRequestDTO dto) {
    	
    	// Vérifie si le nom d'item existe déjà
        if (itemRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Un item avec le nom '" + dto.name() + "' existe déjà");
        }

        Item item = Item.builder()
                .name(dto.name())
                .description(dto.description())
                .type(dto.type())
                .bonus(dto.bonus())
                .stat(dto.stat())
                .rarity(dto.rarity())
                .build();

        return ItemResponseDTO.from(itemRepository.save(item));
    }

    // Met à jour l'item
    public ItemResponseDTO update(UUID id, ItemRequestDTO dto) {
   
    	// Vérifie que l'item existe
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item introuvable avec l'id : " + id));

        // Vérifie si le nouveau nom d'item existe déjà
        if (!item.getName().equals(dto.name()) && itemRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Un item avec le nom '" + dto.name() + "' existe déjà");
        }

        item.setName(dto.name());
        item.setDescription(dto.description());
        item.setType(dto.type());
        item.setBonus(dto.bonus());
        item.setStat(dto.stat());
        item.setRarity(dto.rarity());

        return ItemResponseDTO.from(itemRepository.save(item));
    }

    // Supprime l'item récupéré par son ID
    public void delete(UUID id) {
    	
    	// Vérifie que l'item existe
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item introuvable avec l'id : " + id);
        }
        itemRepository.deleteById(id);
    }
}
