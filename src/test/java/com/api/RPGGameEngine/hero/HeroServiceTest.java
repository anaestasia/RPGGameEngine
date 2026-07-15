package com.api.RPGGameEngine.hero;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.RPGGameEngine.common.enums.HeroRace;
import com.api.RPGGameEngine.common.enums.HeroRole;
import com.api.RPGGameEngine.common.exceptions.ResourceNotFoundException;
import com.api.RPGGameEngine.hero.dto.HeroRequestDTO;
import com.api.RPGGameEngine.hero.dto.HeroResponseDTO;


@ExtendWith(MockitoExtension.class)
public class HeroServiceTest {
	
	@Mock // Créé un faux objet qui ne fait rien par défaut
	private HeroRepository heroRepository;
	
	@InjectMocks // crée une vraie instance de la classe testée et y injecte automatiquement les @Mock déclarés au-dessus
	private HeroService heroService;
	
	private Hero hero;
    private UUID heroId;

    @BeforeEach // exécute setUp() avant chaque test, pour repartir sur une donnée propre
    void setUp() {
        heroId = UUID.fromString("00000000-0000-0000-0001-000000000001");
        hero = Hero.builder()
                .id(heroId)
                .name("Hero Test")
                .race(HeroRace.DWARF)
                .role(HeroRole.THIEF)
                .build();
    }
    
    /* ---------------------------------
	   ----------- FindAll -------------
	   --------------------------------- */
	 
	 // Cas : aucun filtre
	 @Test
	 void findAll_shouldReturnAllHeroes_whenHeroExists() {
	 	 // Arrange
		 given(heroRepository.findAll()).willReturn(List.of(hero));
		
		 // Act
		 List<HeroResponseDTO> result = heroService.findAll();
		
		 // Assert
		 assertThat(result).hasSize(1);
		 verify(heroRepository).findAll();
	 }
	 
	// Cas : pas de résultats
    @Test
    void findAll_shouldReturnEmptyList_whenNoHeroExists() {
        // Arrange
        given(heroRepository.findAll()).willReturn(List.of());

        // Act
        List<HeroResponseDTO> result = heroService.findAll();

        // Assert
        assertThat(result).isNotNull();  // ne doit pas retourner null
        assertThat(result).isEmpty();    // mais une liste vide
    }
    
    /* --------------------------------
	    ----------- FindById ----------- 
	    -------------------------------- */
	 
	 // Cas : hero trouvé
	 @Test
	 void findById_shouldReturnHero_whenHeroExists() {
	    // Arrange
	    given(heroRepository.findById(heroId)).willReturn(Optional.of(hero));
	
	    // Act
	    HeroResponseDTO result = heroService.findById(heroId);
	
	    // Assert
	    assertThat(result).isNotNull();
	    assertThat(result.name()).isEqualTo(hero.getName());
	 }
	 
	 // Cas : hero introuvable
	 @Test
	 void findById_shouldThrowException_whenHeroNotFound() { 
	    // Arrange
	    given(heroRepository.findById(heroId)).willReturn(Optional.empty());
	
	    // Act / Assert
	    assertThatThrownBy(() -> heroService.findById(heroId))
	            .isInstanceOf(ResourceNotFoundException.class)
	            .hasMessageContaining("Personnage introuvable avec l'id : " + heroId);
	
	    // On vérifie qu'il n'y a pas eu de save()
	    verify(heroRepository, never()).save(any());
	 }
	 
    /* --------------------------------
 	   ----------- Create -------------
 	   -------------------------------- */

	// Cas : nom disponible
	@Test
	void create_shouldReturnCreatedHero_whenNameisAvailable() {
	  	
		// Arrange
		HeroRequestDTO dto = new HeroRequestDTO(
		          hero.getName(),
		          hero.getRace(),
		          hero.getRole()
		);
		given(heroRepository.existsByName(dto.name())).willReturn(false);
		given(heroRepository.save(any(Hero.class))).willReturn(hero); // any(Hero.class) : Accepte n'importe quel hero
		
		
		// Act
		HeroResponseDTO result = heroService.create(dto);
		
		// Assert
		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo(dto.name());
		verify(heroRepository).save(any(Hero.class));
	}
	
	// Cas : nom déjà pris
    @Test
    void create_shouldThrowException_whenNameAlreadyExists() {
        // Arrange
    	HeroRequestDTO dto = new HeroRequestDTO(
		          "JohnDoe", // Déjà pris
		          hero.getRace(),
		          hero.getRole()       
		  );
        given(heroRepository.existsByName(dto.name())).willReturn(true);

        // Act / Assert
        assertThatThrownBy(() -> heroService.create(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Un personnage avec le nom '" + dto.name() + "' existe déjà");

        // Le save ne doit jamais être atteint
        verify(heroRepository, never()).save(any());
    }
    
    /* --------------------------------
    ----------- Update -------------
    -------------------------------- */
 
	// Cas : nom disponible + hero trouvé
	@Test
	void update_shouldReturnUpdatedHero_whenHeroExistsAndNameIsAvailable() {
		// Arrange
		HeroRequestDTO dto = new HeroRequestDTO(
		        "John Doe", // Nouveau nom
		        HeroRace.ELF, // Nouvelle race (pour vérifier le recalcule des stats)
		        hero.getRole()
		);
		given(heroRepository.findById(heroId)).willReturn(Optional.of(hero));
		given(heroRepository.existsByName(dto.name())).willReturn(false);
		given(heroRepository.save(any(Hero.class))).willReturn(hero);
		
		 // Act
		HeroResponseDTO result = heroService.update(heroId, dto);
		
		 // Assert
		assertThat(result).isNotNull();
		verify(heroRepository).save(any(Hero.class));
	}
 
 // Cas : hero introuvable
 @Test
 void update_shouldThrowException_whenHeroNotFound() {
     // Arrange
     HeroRequestDTO dto = new HeroRequestDTO(
    		 	"John Doe", // Nouveau nom
		        HeroRace.ELF, // Nouvelle race (pour vérifier le recalcule des stats)
		        hero.getRole()
     );
     given(heroRepository.findById(heroId)).willReturn(Optional.empty());

     // Act / Assert
     assertThatThrownBy(() -> heroService.update(heroId, dto))
             .isInstanceOf(ResourceNotFoundException.class)
             .hasMessageContaining("Personnage introuvable avec l'id : " + heroId);

     verify(heroRepository, never()).save(any());
 }
 
 // Cas : nom déjà pris
 @Test
 void update_shouldThrowException_whenNewNameAlreadyExists() {
     // Arrange
     HeroRequestDTO dto = new HeroRequestDTO(
    		 	"John Doe", // Nom déjà pris
		        HeroRace.ELF, // Nouvelle race
		        hero.getRole()
     );
     given(heroRepository.findById(heroId)).willReturn(Optional.of(hero));
     given(heroRepository.existsByName(dto.name())).willReturn(true);

     // Act / Assert
     assertThatThrownBy(() -> heroService.update(heroId, dto))
             .isInstanceOf(IllegalArgumentException.class)
             .hasMessageContaining("Un personnage avec le nom '" + dto.name() + "' existe déjà");

     verify(heroRepository, never()).save(any());
 }
 
 /* --------------------------------
    ----------- Delete -------------
    -------------------------------- */
 
 // Cas : hero trouvé
 @Test
 void delete_shouldDeleteHero_whenHeroExists() {
     // Arrange
     given(heroRepository.existsById(heroId)).willReturn(true);

     // Act
     heroService.delete(heroId);

     // Assert — delete retourne void, donc on vérifie que deleteById a bien été appelé
     verify(heroRepository).deleteById(heroId);
 }
 
 
 // Cas : hero introuvable
 @Test
 void delete_shouldThrowException_whenHeroNotFound() {
     // Arrange
     given(heroRepository.existsById(heroId)).willReturn(false);

     // Act / Assert
     assertThatThrownBy(() -> heroService.delete(heroId))
             .isInstanceOf(ResourceNotFoundException.class)
             .hasMessageContaining("Personnage introuvable avec l'id : " + heroId);

     verify(heroRepository, never()).deleteById(any());
 }
}