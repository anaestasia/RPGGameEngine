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
import com.api.RPGGameEngine.common.exceptions.ApiError;
import com.api.RPGGameEngine.inventory.dto.InventoryRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Inventory", description = "Gestion de l'inventaire et de l'équipement")
@RestController
@RequestMapping("/heroes")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	/* ----- GET INVENTORY ----- */
	
	@Operation(summary = "Lister l'inventaire d'un personnage",
			description = "Retourne tous les items possédés, équipés ou non")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Item(s) possédé(s) trouvé(s) avec succès",
	                content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))),
	        @ApiResponse(responseCode = "404", description = "Personnage introuvable",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@GetMapping("/{heroId}/inventory")
    public ResponseEntity<List<InventoryResponseDTO>> getInventory(@PathVariable UUID heroId) {

        return ResponseEntity.ok(inventoryService.getInventory(heroId));
    }
	
	/* ----- GET EQUIPMENT ----- */
	
	@Operation(summary = "Lister l'équipement d'un personnage",
			description = "Retourne tous les items où equipped = true")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Item(s) équipé(s) trouvé(s) avec succès",
	                content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))),
	        @ApiResponse(responseCode = "404", description = "Personnage introuvable",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@GetMapping("/{heroId}/equipment")
    public ResponseEntity<List<InventoryResponseDTO>> getEquipment(@PathVariable UUID heroId) {

        return ResponseEntity.ok(inventoryService.getEquipment(heroId));
    }
	
	/* ----- ADD TO INVENTORY ----- */
	
	@Operation(summary = "Ajoute un item à l'inventaire d'un personnage",
			description = "L'item est ajouté avec equipped = false")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Item ajouté à l'inventaire avec succès",
	                content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))),
	        @ApiResponse(responseCode = "404", description = "Personnage ou Item introuvable",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@PostMapping("/{heroId}/inventory")
    public ResponseEntity<InventoryResponseDTO> addToInventory(@PathVariable UUID heroId, @Valid @RequestBody InventoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.addToInventory(heroId, dto));
    }
	
	/* ----- EQUIP AN ITEM ----- */
	
	@Operation(summary = "Equipe l'item à un personnage depuis l'inventaire",
			description = "L'association est modifiée avec equipped = true dans le slot correspondant")
	@PutMapping("/{heroId}/equip")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Item équipé avec succès",
	                content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))),
	        @ApiResponse(responseCode = "400", description = "Item invalide ou consommable non équipable",
	                content = @Content(schema = @Schema(implementation = ApiError.class))),
	        @ApiResponse(responseCode = "404", description = "Personnage, item ou association introuvable",
	                content = @Content(schema = @Schema(implementation = ApiError.class))),
	        @ApiResponse(responseCode = "409", description = "Slot déjà occupé ou item déjà équipé",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
    public ResponseEntity<InventoryResponseDTO> equip(@PathVariable UUID heroId, @Valid @RequestBody InventoryRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.equip(heroId, dto));
	}
	
	/* ----- UNEQUIP AN ITEM ----- */
	
	@Operation(summary = "Déséquipe l'item d'un personnage, reste dans l'inventaire",
			description = "L'association est modifiée avec equipped = false")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Item déséquipé avec succès",
	                content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))),
	        @ApiResponse(responseCode = "404", description = "Personnage, item ou association introuvable",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@PutMapping("/{heroId}/unequip")
    public ResponseEntity<InventoryResponseDTO> unequip(@PathVariable UUID heroId, @Valid @RequestBody InventoryRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.unequip(heroId, dto));
	}
	
	/* ----- DELETE FROM INVENTORY ----- */
	
	@Operation(summary = "Supprimer un item de l'inventaire d'un personnage",
			description = "Supprime définitivement l'association")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "204", description = "Item supprimé de l'inventaire avec succès",
	                content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))),
	        @ApiResponse(responseCode = "404", description = "Personnage, item ou association introuvable",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
    @DeleteMapping("/{heroId}/inventory/{itemId}")
    public ResponseEntity<Void> removeFromInventory(@PathVariable UUID heroId, @PathVariable UUID itemId) {
		inventoryService.removeFromInventory(heroId, itemId);
        return ResponseEntity.noContent().build();
    }

}
