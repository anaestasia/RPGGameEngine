package com.api.RPGGameEngine.inventory;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.RPGGameEngine.inventory.dto.InventoryResponseDTO;
import com.api.RPGGameEngine.inventory.dto.InventoryRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Inventory", description = "Gestion de l'inventaire et de l'équipement")
@RestController
@RequestMapping("/heroes")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	@Operation(summary = "Lister l'inventaire d'un personnage",
			description = "Retourne tous les items possédés, équipés ou non")
	@GetMapping("/{heroId}/inventory")
    public ResponseEntity<List<InventoryResponseDTO>> getInventory(@PathVariable UUID heroId) {

        return ResponseEntity.ok(inventoryService.getInventory(heroId));
    }
	
	@Operation(summary = "Lister l'équipement d'un personnage",
			description = "Retourne tous les items où equipped = true")
	@GetMapping("/{heroId}/equipment")
    public ResponseEntity<List<InventoryResponseDTO>> getEquipment(@PathVariable UUID heroId) {

        return ResponseEntity.ok(inventoryService.getEquipment(heroId));
    }
	
	@Operation(summary = "Ajoute un item à l'inventaire d'un personnage",
			description = "L'item est ajouté avec equipped = false dans le slot INVENTORY")
	@PostMapping("/{heroId}/inventory")
    public ResponseEntity<InventoryResponseDTO> addToInventory(@PathVariable UUID heroId, @Valid @RequestBody InventoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.addToInventory(heroId, dto));
    }
	
	@Operation(summary = "Equipe l'item à un personnage depuis l'inventaire",
			description = "L'association est modifiée avec equipped = true dans le slot correspondant")
	@PutMapping("/{heroId}/equip")
    public ResponseEntity<InventoryResponseDTO> equip(@PathVariable UUID heroId, @Valid @RequestBody InventoryRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.equip(heroId, dto));
	}
	
	@Operation(summary = "Déséquipe l'item d'un personnage, reste dans l'inventaire",
			description = "L'association est modifiée avec equipped = false dans le slot INVENTORY")
	@PutMapping("/{heroId}/unequip")
    public ResponseEntity<InventoryResponseDTO> unequip(@PathVariable UUID heroId, @Valid @RequestBody InventoryRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.unequip(heroId, dto));
	}
	
	@Operation(summary = "Supprimer un item de l'inventaire d'un personnage",
			description = "Supprime définitivement l'association")
    @DeleteMapping("/{heroId}/inventory/{itemId}")
    public ResponseEntity<Void> removeFromInventory(@PathVariable UUID heroId, @PathVariable UUID itemId) {
		inventoryService.removeFromInventory(heroId, itemId);
        return ResponseEntity.noContent().build();
    }

}
