package com.api.RPGGameEngine.item;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.RPGGameEngine.common.enums.ItemRarity;
import com.api.RPGGameEngine.common.enums.ItemType;
import com.api.RPGGameEngine.common.exceptions.ApiError;
import com.api.RPGGameEngine.item.dto.ItemRequestDTO;
import com.api.RPGGameEngine.item.dto.ItemResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Items", description = "Gestion des items")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemService itemService;

	/* ----- FIND ITEMS BY FILTERS ----- */
	
	@Operation(summary = "Lister tous les items", description = "Retourne la liste complète ou filtrée par type ou par rareté")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Item(s) trouvé(s) avec succès",
	                content = @Content(schema = @Schema(implementation = ItemResponseDTO.class)))
	})
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAll(
            @RequestParam(required = false) ItemType type,
            @RequestParam(required = false) ItemRarity rarity) {

		List<ItemResponseDTO> items = itemService.findByFilters(type, rarity);

        return ResponseEntity.ok(items);
    }
	
	/* ----- FIND ITEM BY ID ----- */

	@Operation(summary = "Récupérer un item par ID")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Item trouvé avec succès",
	                content = @Content(schema = @Schema(implementation = ItemResponseDTO.class))),
	        @ApiResponse(responseCode = "404", description = "Item introuvable",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

	/* ----- CREATE AN ITEM ----- */
	
	@Operation(summary = "Créer un item")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Item créé avec succès",
	                content = @Content(schema = @Schema(implementation = ItemResponseDTO.class))),
	        @ApiResponse(responseCode = "400", description = "Données invalides",
	                content = @Content(schema = @Schema(implementation = ApiError.class))),
	        @ApiResponse(responseCode = "409", description = "Un item avec ce nom existe déjà",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
    @PostMapping
    public ResponseEntity<ItemResponseDTO> create(@Valid @RequestBody ItemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(dto));
    }

	/* ----- UPDATE AN ITEM ----- */
	
	@Operation(summary = "Modifier un item")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Item modifié avec succès",
	                content = @Content(schema = @Schema(implementation = ItemResponseDTO.class))),
	        @ApiResponse(responseCode = "400", description = "Données invalides",
	                content = @Content(schema = @Schema(implementation = ApiError.class))),
	        @ApiResponse(responseCode = "404", description = "Item introuvable",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
	        @ApiResponse(responseCode = "409", description = "Un item avec ce nom existe déjà",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ItemRequestDTO dto) {
        return ResponseEntity.ok(itemService.update(id, dto));
    }

	/* ----- DELETE AN ITEM ----- */
	
	@Operation(summary = "Supprimer un item")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "204", description = "Item supprimé avec succès"),
	        @ApiResponse(responseCode = "404", description = "Item introuvable",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
