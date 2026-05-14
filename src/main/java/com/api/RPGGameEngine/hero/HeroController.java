package com.api.RPGGameEngine.hero;

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

import com.api.RPGGameEngine.hero.dto.HeroResponseDTO;
import com.api.RPGGameEngine.hero.dto.XpRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.api.RPGGameEngine.hero.dto.HeroRequestDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Heroes", description = "Gestion des héros")
@RestController
@RequestMapping("/heroes")
@RequiredArgsConstructor
public class HeroController {
	
	private final HeroService heroService;
	
	@Operation(summary = "Lister tous les personnages")
	@GetMapping
    public ResponseEntity<List<HeroResponseDTO>> getAll() {

        return ResponseEntity.ok(heroService.findAll());
    }
	
	@Operation(summary = "Récupérer un personnage par ID")
	@GetMapping("/{id}")
    public ResponseEntity<HeroResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(heroService.findById(id));
    }
	
	@Operation(summary = "Créer un personnage")
	@PostMapping
    public ResponseEntity<HeroResponseDTO> create(@Valid @RequestBody HeroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(heroService.create(dto));
    }
	
	// On utilise POST et pas PUT car PUT est idempotent (si on l'appelle plusieurs fois de suite, le résultat est différent : incrémente l'xp)
	@Operation(summary = "Ajouter de l'XP à un personnage")
	@PostMapping("/{id}/xp")
    public ResponseEntity<HeroResponseDTO> addExperience(
    		@PathVariable UUID id,
    		@Valid @RequestBody XpRequestDTO dto) {
        return ResponseEntity.ok(heroService.addExperience(id, dto));
    }
	
	@Operation(summary = "Modifier un personnage")
	@PutMapping("/{id}")
    public ResponseEntity<HeroResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody HeroRequestDTO dto) {
        return ResponseEntity.ok(heroService.update(id, dto));
    }

	@Operation(summary = "Supprimer un personnage")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        heroService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
