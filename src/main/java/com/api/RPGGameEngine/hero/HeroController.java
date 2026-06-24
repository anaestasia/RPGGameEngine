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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.api.RPGGameEngine.common.exceptions.ApiError;
import com.api.RPGGameEngine.hero.dto.HeroRequestDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Heroes", description = "Gestion des héros")
@RestController
@RequestMapping("/heroes")
@RequiredArgsConstructor
public class HeroController {
	
	private final HeroService heroService;
	
	/* ----- FIND ALL HEROES ----- */
	
	@Operation(summary = "Lister tous les personnages")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Personnages trouvés avec succès",
	                content = @Content(schema = @Schema(implementation = HeroResponseDTO.class)))
	})
	@GetMapping
    public ResponseEntity<List<HeroResponseDTO>> getAll() {

        return ResponseEntity.ok(heroService.findAll());
    }
	
	/* ----- FIND A HERO BY ID ----- */
	
	@Operation(summary = "Récupérer un personnage par ID")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Personnage trouvé avec succès",
	                content = @Content(schema = @Schema(implementation = HeroResponseDTO.class))),
	        @ApiResponse(responseCode = "404", description = "Personnage introuvable",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@GetMapping("/{id}")
    public ResponseEntity<HeroResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(heroService.findById(id));
    }
	
	/* ----- CREATE A HERO ----- */
	
	@Operation(summary = "Créer un personnage")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Personnage créé avec succès",
	                content = @Content(schema = @Schema(implementation = HeroResponseDTO.class))),
	        @ApiResponse(responseCode = "400", description = "Données invalides",
	                content = @Content(schema = @Schema(implementation = ApiError.class))),
	        @ApiResponse(responseCode = "409", description = "Un personnage avec ce nom existe déjà",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@PostMapping
    public ResponseEntity<HeroResponseDTO> create(@Valid @RequestBody HeroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(heroService.create(dto));
    }
	
	/* ----- ADD XP TO A HERO ----- */
	
	@Operation(summary = "Ajouter de l'XP à un personnage")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Expérience ajoutée avec succès",
	                content = @Content(schema = @Schema(implementation = HeroResponseDTO.class))),
	        @ApiResponse(responseCode = "404", description = "Personnage introuvable",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@PostMapping("/{id}/xp")
	// On utilise POST et pas PUT car PUT est idempotent
	// (si on l'appelle plusieurs fois de suite, le résultat est différent : incrémente l'xp)
    public ResponseEntity<HeroResponseDTO> addExperience(
    		@PathVariable UUID id,
    		@Valid @RequestBody XpRequestDTO dto) {
        return ResponseEntity.ok(heroService.addExperience(id, dto));
    }
	
	/* ----- UPDATE A HERO ----- */
	
	@Operation(summary = "Modifier un personnage")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Personnage modifié avec succès",
	                content = @Content(schema = @Schema(implementation = HeroResponseDTO.class))),
	        @ApiResponse(responseCode = "400", description = "Données invalides",
	                content = @Content(schema = @Schema(implementation = ApiError.class))),
	        @ApiResponse(responseCode = "404", description = "Personnage introuvable",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
	        @ApiResponse(responseCode = "409", description = "Un personnage avec ce nom existe déjà",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@PutMapping("/{id}")
    public ResponseEntity<HeroResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody HeroRequestDTO dto) {
        return ResponseEntity.ok(heroService.update(id, dto));
    }

	/* ----- DELETE A HERO ----- */
	
	@Operation(summary = "Supprimer un personnage")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "204", description = "Personnage supprimé avec succès"),
	        @ApiResponse(responseCode = "404", description = "Personnage introuvable",
	                content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        heroService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
