package com.api.RPGGameEngine.hero;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.RPGGameEngine.common.exceptions.ResourceNotFoundException;
import com.api.RPGGameEngine.hero.dto.HeroRequestDTO;
import com.api.RPGGameEngine.hero.dto.HeroResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeroService {
	
	private final HeroRepository heroRepository;
	
	// Liste tous les personnages
	public List<HeroResponseDTO> findAll(){
		
		return heroRepository.findAll()
				.stream()
				.map(HeroResponseDTO::from)
				.toList();
	}
	
	// Récupère le personnage par son ID
    public HeroResponseDTO findById(UUID id) {
    	
        return heroRepository.findById(id)
                .map(HeroResponseDTO::from)
                .orElseThrow(() -> new ResourceNotFoundException("Personnage introuvable avec l'id : " + id));
    }
    
 // Créer le peronnage
    public HeroResponseDTO create(HeroRequestDTO dto) {
    	
    	// Vérifie si le nom de personnage existe déjà
        if (heroRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Un personnage avec le nom '" + dto.name() + "' existe déjà");
        }

        Hero hero = Hero.builder()
                .name(dto.name())
                .race(dto.race())
                .role(dto.role())
                .build();

        return HeroResponseDTO.from(heroRepository.save(hero));
    }
    
 // Met à jour le personnage
    public HeroResponseDTO update(UUID id, HeroRequestDTO dto) {
   
    	// Vérifie que le personnage existe
        Hero hero = heroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personnage introuvable avec l'id : " + id));

        // Vérifie si le nouveau nom de perosnnage existe déjà
        if (!hero.getName().equals(dto.name()) && heroRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Un personnage avec le nom '" + dto.name() + "' existe déjà");
        }

        hero.setName(dto.name());
        hero.setRace(dto.race());
        hero.setRole(dto.role());

        return HeroResponseDTO.from(heroRepository.save(hero));
    }

    // Supprime le personnage récupéré par son ID
    public void delete(UUID id) {
    	
    	// Vérifie que le personnage existe
        if (!heroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Personnage introuvable avec l'id : " + id);
        }
        heroRepository.deleteById(id);
    }

}
