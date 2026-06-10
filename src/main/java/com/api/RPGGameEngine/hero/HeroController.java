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
import com.api.RPGGameEngine.hero.dto.HeroRequestDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/heroes")
@RequiredArgsConstructor
public class HeroController {
	
	private final HeroService heroService;
	
	@GetMapping
    public ResponseEntity<List<HeroResponseDTO>> getAll() {

        return ResponseEntity.ok(heroService.findAll());
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<HeroResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(heroService.findById(id));
    }
	
	@PostMapping
    public ResponseEntity<HeroResponseDTO> create(@Valid @RequestBody HeroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(heroService.create(dto));
    }
	
	@PostMapping("/{id}/xp")
    public ResponseEntity<HeroResponseDTO> addExperience(
    		@PathVariable UUID id,
    		@Valid @RequestBody XpRequestDTO dto) {
        return ResponseEntity.ok(heroService.addExperience(id, dto));
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<HeroResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody HeroRequestDTO dto) {
        return ResponseEntity.ok(heroService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        heroService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
