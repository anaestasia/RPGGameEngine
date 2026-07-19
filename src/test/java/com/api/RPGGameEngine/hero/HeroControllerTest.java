package com.api.RPGGameEngine.hero;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.api.RPGGameEngine.common.enums.HeroRace;
import com.api.RPGGameEngine.common.enums.HeroRole;
import com.api.RPGGameEngine.common.exceptions.ResourceNotFoundException;
import com.api.RPGGameEngine.hero.dto.HeroRequestDTO;
import com.api.RPGGameEngine.hero.dto.HeroResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(HeroController.class) // Démarre un contexte spring partiel
class HeroControllerTest{
	
	@Autowired
	private MockMvc mockMvc; // Simule les requêtes HTTP sans démarrer de serveur
	
	@Autowired
	private ObjectMapper objectMapper; // sérialise/désérialise le JSON
	
	@MockBean // Enregistre le mock dans le contexte spring
	private HeroService heroService;
	
	private UUID heroId;
    private HeroResponseDTO heroResponse;
    private HeroRequestDTO heroRequest;
    
    @BeforeEach
    void setUp() {
    	heroId = UUID.fromString("00000000-0000-0000-0001-000000000001");

    	heroResponse = new HeroResponseDTO(
    			heroId,
                "Esquie",
                10, // PV
                2, // ATQ
                0, // DEF (+2 bard)
                1, // SPEED  (+1 elf)
                1, // LVL hero.getLevel() ?
                0, // EXP
                100, // EXP to next lvl hero.expToNextLevel(hero.getExp()) ?
                HeroRace.ELF,
                HeroRole.BARD
                                
        );

    	heroRequest = new HeroRequestDTO(
                "Esquie",
                HeroRace.ELF,
                HeroRole.BARD
        );
    }
    
    /* --------------------------------
       ----------- Get All ------------ 
       -------------------------------- */
    
    // Cas : résultats trouvés
	@Test
    void getAll_shouldReturnListOfHeroes_whenHeroesExist() throws Exception {
        // Arrange
        given(heroService.findAll()).willReturn(List.of(heroResponse));

        // Act / Assert
        mockMvc.perform(get("/heroes")
                .contentType(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$[0].id").value(heroId.toString()))
        	.andExpect(jsonPath("$[0].name").value("Esquie"))
        	.andExpect(jsonPath("$[0].pv").value(10))
        	.andExpect(jsonPath("$[0].atq").value(2))
        	.andExpect(jsonPath("$[0].def").value(0))
        	.andExpect(jsonPath("$[0].speed").value(1))
        	.andExpect(jsonPath("$[0].level").value(1))
        	.andExpect(jsonPath("$[0].exp").value(0))
        	.andExpect(jsonPath("$[0].expToNextLevel").value(100))
        	.andExpect(jsonPath("$[0].race").value("ELF"))
        	.andExpect(jsonPath("$[0].role").value("BARD"));
    }
    
    // Cas : pas de résultats
    @Test
    void getAll_shouldReturnEmptyList_whenNoHeroExists() throws Exception {
        // Arrange
        given(heroService.findAll()).willReturn(List.of());

        // Act / Assert
        mockMvc.perform(get("/heroes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()) // Vérifie que c'est bien un tableau JSON
                .andExpect(jsonPath("$").isEmpty()); // Mais qu'il est bien vide
    }
    
    /* --------------------------------
       ----------- Get by ID ---------- 
       -------------------------------- */
 
	//Cas : Hero trouvé
	@Test
	void getById_shouldReturnHero_WhenHeroExists() throws Exception {
		// Arrange
	 	given(heroService.findById(heroId)).willReturn(heroResponse);
	 	
	 	// Act / Assert
	 	mockMvc.perform(get("/heroes/{id}", heroId)
	 			.contentType(MediaType.APPLICATION_JSON))
	 		.andExpect(status().isOk())
	 		.andExpect(jsonPath("$.id").value(heroId.toString()))
	 		.andExpect(jsonPath("$.name").value("Esquie"))
	 		.andExpect(jsonPath("$.pv").value(10))
        	.andExpect(jsonPath("$.atq").value(2))
        	.andExpect(jsonPath("$.def").value(0))
        	.andExpect(jsonPath("$.speed").value(1))
        	.andExpect(jsonPath("$.level").value(1))
        	.andExpect(jsonPath("$.exp").value(0))
        	.andExpect(jsonPath("$.expToNextLevel").value(100))
        	.andExpect(jsonPath("$.race").value("ELF"))
        	.andExpect(jsonPath("$.role").value("BARD"));
	}
	 
	// Cas : hero introuvable
    @Test
    void getById_shouldReturn404_whenHeroNotFound() throws Exception {
        // Arrange
        given(heroService.findById(heroId))
        	.willThrow(new ResourceNotFoundException("Personnage introuvable avec l'id : " + heroId));

        // Act / Assert
        mockMvc.perform(get("/heroes/{id}", heroId)
        		.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(status().isNotFound());
    }
    
    /* --------------------------------
       ------------ Create ------------ 
       -------------------------------- */
 
	 // Cas : nom disponible
	@Test
	void create_shouldReturn201_whenHeroIsCreated() throws Exception {
		// Arrange
		given(heroService.create(any(HeroRequestDTO.class))).willReturn(heroResponse);
	
		// Act / Assert
		mockMvc.perform(post("/heroes")
	     		.contentType(MediaType.APPLICATION_JSON)
	             .content(objectMapper.writeValueAsString(heroRequest)))
	     	.andExpect(status().isCreated())
	     	.andExpect(jsonPath("$.id").value(heroId.toString()))
	     	.andExpect(jsonPath("$.name").value("Esquie"))
	     	.andExpect(jsonPath("$.pv").value(10))
        	.andExpect(jsonPath("$.atq").value(2))
        	.andExpect(jsonPath("$.def").value(0))
        	.andExpect(jsonPath("$.speed").value(1))
        	.andExpect(jsonPath("$.level").value(1))
        	.andExpect(jsonPath("$.exp").value(0))
        	.andExpect(jsonPath("$.expToNextLevel").value(100))
        	.andExpect(jsonPath("$.race").value("ELF"))
        	.andExpect(jsonPath("$.role").value("BARD"));
	}
	
	// Cas : nom déjà pris
	@Test
	void create_shouldReturn409_whenNameAlreadyExists() throws Exception {
	    // Arrange
	    given(heroService.create(any(HeroRequestDTO.class)))
	    	.willThrow(new IllegalArgumentException("Un personnage avec le nom 'Esquie' existe déjà"));
	
	    // Act / Assert
	    mockMvc.perform(post("/heroes")
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(objectMapper.writeValueAsString(heroRequest)))
	   		.andExpect(status().isConflict());
	}
	 
	/* --------------------------------
       ------------ Update ------------ 
       -------------------------------- */
  
	// Cas : hero trouvé + nom valide
	@Test
	void update_shouldReturn200_whenHeroIsUpdated() throws Exception {
		// Arrange
		// (eq() => exactement cette valeur / any() => n'importe quelle valeur de ce type)
	    given(heroService.update(eq(heroId), any(HeroRequestDTO.class))).willReturn(heroResponse);
	
	    // Act / Assert
	    mockMvc.perform(put("/heroes/{id}", heroId)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(objectMapper.writeValueAsString(heroRequest)))
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("$.id").value(heroId.toString()))
	      	.andExpect(jsonPath("$.name").value("Esquie"))
	     	.andExpect(jsonPath("$.pv").value(10))
        	.andExpect(jsonPath("$.atq").value(2))
        	.andExpect(jsonPath("$.def").value(0))
        	.andExpect(jsonPath("$.speed").value(1))
        	.andExpect(jsonPath("$.level").value(1))
        	.andExpect(jsonPath("$.exp").value(0))
        	.andExpect(jsonPath("$.expToNextLevel").value(100))
        	.andExpect(jsonPath("$.race").value("ELF"))
        	.andExpect(jsonPath("$.role").value("BARD"));
	}
	  
	// Cas : hero introuvable
	@Test
	void update_shouldReturn404_whenHeroNotFound() throws Exception {
		// Arrange
	    given(heroService.update(eq(heroId), any(HeroRequestDTO.class)))
	    	.willThrow(new ResourceNotFoundException("Personnage introuvable avec l'id : " + heroId));
	
	    // Act / Assert
	    mockMvc.perform(put("/heroes/{id}", heroId)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(objectMapper.writeValueAsString(heroRequest)))
	    	.andExpect(status().isNotFound());
	}
	
	// Cas : nom déjà pris
	@Test
	void update_shouldReturn409_whenNameAlreadyExists() throws Exception {
	    // Arrange
	    given(heroService.update(eq(heroId), any(HeroRequestDTO.class)))
	    	.willThrow(new IllegalArgumentException("Un personnage avec le nom 'Esquie' existe déjà"));
	
	    // Act / Assert
	    mockMvc.perform(put("/heroes/{id}", heroId)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(objectMapper.writeValueAsString(heroRequest)))
	    	.andExpect(status().isConflict());
	}
	
	/* --------------------------------
    ------------ Delete ------------ 
    -------------------------------- */
 
	// Cas : hero trouvé
	@Test
	void delete_shouldReturn204_whenHeroIsDeleted() throws Exception {
	    // Arrange
		willDoNothing().given(heroService).delete(heroId); // Pas de return => void
	
		// Act / Assert
		mockMvc.perform(delete("/heroes/{id}", heroId))
	    	.andExpect(status().isNoContent());
	}
	
	// Cas : hero introuvable
	@Test
	void delete_shouldReturn404_whenHeroNotFound() throws Exception {
	    // Arrange
	willThrow(new ResourceNotFoundException("Personnage introuvable avec l'id : " + heroId))
		.given(heroService).delete(heroId);
	
	// Act / Assert
	mockMvc.perform(delete("/heroes/{id}", heroId))
	    	.andExpect(status().isNotFound());
	}
}