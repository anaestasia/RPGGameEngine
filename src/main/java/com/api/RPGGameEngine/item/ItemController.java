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
import com.api.RPGGameEngine.item.dto.ItemRequestDTO;
import com.api.RPGGameEngine.item.dto.ItemResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Items", description = "Gestion des items")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemService itemService;

	@Operation(summary = "Lister tous les items", description = "Retourne la liste complète ou filtrée par type ou par rareté")
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAll(
            @RequestParam(required = false) ItemType type,
            @RequestParam(required = false) ItemRarity rarity) {

		List<ItemResponseDTO> items = itemService.findByFilters(type, rarity);

        return ResponseEntity.ok(items);
    }

	@Operation(summary = "Récupérer un item par ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

	@Operation(summary = "Créer un item")
    @PostMapping
    public ResponseEntity<ItemResponseDTO> create(@Valid @RequestBody ItemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(dto));
    }

	@Operation(summary = "Modifier un item")
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ItemRequestDTO dto) {
        return ResponseEntity.ok(itemService.update(id, dto));
    }

	@Operation(summary = "Supprimer un item")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
