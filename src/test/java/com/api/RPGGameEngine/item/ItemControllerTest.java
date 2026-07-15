package com.api.RPGGameEngine.item;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.api.RPGGameEngine.common.enums.ItemRarity;
import com.api.RPGGameEngine.common.enums.ItemType;
import com.api.RPGGameEngine.common.enums.StatType;
import com.api.RPGGameEngine.common.exceptions.ResourceNotFoundException;
import com.api.RPGGameEngine.item.dto.ItemRequestDTO;
import com.api.RPGGameEngine.item.dto.ItemResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ItemController.class) // Démarre un contexte Spring partiel (controller, filtres, sérialisation JSON)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simule les requêtes HTTP sans démarrer de serveur

    @Autowired
    private ObjectMapper objectMapper; // sérialise/désérialise le JSON

    @MockBean // Enregistre le mock dans le contexte Spring
    private ItemService itemService;

    private UUID itemId;
    private ItemResponseDTO itemResponse;
    private ItemRequestDTO itemRequest;

    @BeforeEach
    void setUp() {
        itemId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        itemResponse = new ItemResponseDTO(
                itemId,
                "Épée rouillée",
                "Une vieille épée",
                ItemType.ONE_HAND_WEAPON,
                5,
                StatType.ATQ,
                ItemRarity.COMMON
        );

        itemRequest = new ItemRequestDTO(
                "Épée rouillée",
                "Une vieille épée",
                ItemType.ONE_HAND_WEAPON,
                5,
                StatType.ATQ,
                ItemRarity.COMMON
        );
    }
    
    /* --------------------------------
       ----------- Get All ------------ 
       -------------------------------- */
    
    // Cas : aucun filtre
    @Test
    void getAll_shouldReturnListOfItems_whenNoFilterProvided() throws Exception {
        // Arrange
        given(itemService.findByFilters(null, null)).willReturn(List.of(itemResponse));

        // Act / Assert
        mockMvc.perform(get("/items")
                .contentType(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$[0].id").value(itemId.toString()))
        	.andExpect(jsonPath("$[0].name").value("Épée rouillée"))
        	.andExpect(jsonPath("$[0].description").value("Une vieille épée"))
        	.andExpect(jsonPath("$[0].type").value("ONE_HAND_WEAPON"))
        	.andExpect(jsonPath("$[0].bonus").value(5))
        	.andExpect(jsonPath("$[0].stat").value("ATQ"))
        	.andExpect(jsonPath("$[0].rarity").value("COMMON"));
    }
    
    // Cas : type uniquement
    @Test
    void getAll_shouldReturnFilteredItems_whenTypeProvided() throws Exception {
        // Given
        given(itemService.findByFilters(ItemType.ONE_HAND_WEAPON, null)).willReturn(List.of(itemResponse));

        // When / Then
        mockMvc.perform(get("/items")
        		.param("type", "ONE_HAND_WEAPON")
        		.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$[0].id").value(itemId.toString()))
        	.andExpect(jsonPath("$[0].name").value("Épée rouillée"))
        	.andExpect(jsonPath("$[0].description").value("Une vieille épée"))
        	.andExpect(jsonPath("$[0].type").value("ONE_HAND_WEAPON"))
        	.andExpect(jsonPath("$[0].bonus").value(5))
        	.andExpect(jsonPath("$[0].stat").value("ATQ"))
        	.andExpect(jsonPath("$[0].rarity").value("COMMON"));
    }
    
    // Cas : rareté uniquement
    @Test
    void getAll_shouldReturnFilteredItems_whenOnlyRarityProvided() throws Exception {
        // Given
        given(itemService.findByFilters(null, ItemRarity.COMMON)).willReturn(List.of(itemResponse));

        // When / Then
        mockMvc.perform(get("/items")
        		.param("rarity", "COMMON")
        		.contentType(MediaType.APPLICATION_JSON))
       		.andExpect(status().isOk())
       		.andExpect(jsonPath("$[0].id").value(itemId.toString()))
        	.andExpect(jsonPath("$[0].name").value("Épée rouillée"))
        	.andExpect(jsonPath("$[0].description").value("Une vieille épée"))
        	.andExpect(jsonPath("$[0].type").value("ONE_HAND_WEAPON"))
        	.andExpect(jsonPath("$[0].bonus").value(5))
        	.andExpect(jsonPath("$[0].stat").value("ATQ"))
        	.andExpect(jsonPath("$[0].rarity").value("COMMON"));
    }

    // Cas : type + rareté
    @Test
    void getAll_shouldReturnFilteredItems_whenTypeAndRarityProvided() throws Exception {
        // Given
        given(itemService.findByFilters(ItemType.ONE_HAND_WEAPON, ItemRarity.COMMON))
                .willReturn(List.of(itemResponse));

        // When / Then
        mockMvc.perform(get("/items")
        		.param("type", "ONE_HAND_WEAPON")
        		.param("rarity", "COMMON")
        		.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$[0].id").value(itemId.toString()))
        	.andExpect(jsonPath("$[0].name").value("Épée rouillée"))
        	.andExpect(jsonPath("$[0].description").value("Une vieille épée"))
        	.andExpect(jsonPath("$[0].type").value("ONE_HAND_WEAPON"))
        	.andExpect(jsonPath("$[0].bonus").value(5))
        	.andExpect(jsonPath("$[0].stat").value("ATQ"))
        	.andExpect(jsonPath("$[0].rarity").value("COMMON"));
    }
    
    /* --------------------------------
       ----------- Get by ID ---------- 
       -------------------------------- */
    
    //Cas : item trouvé
    @Test
    void getById_shouldReturnItem_WhenItemExists() throws Exception {
    	// Arrange
    	given(itemService.findById(itemId)).willReturn(itemResponse);
    	
    	// Act / Assert
    	mockMvc.perform(get("/items/{id}", itemId)
    			.contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.id").value(itemId.toString()))
    		.andExpect(jsonPath("$.name").value("Épée rouillée"))
    		.andExpect(jsonPath("$.description").value("Une vieille épée"))
    		.andExpect(jsonPath("$.type").value("ONE_HAND_WEAPON"))
    		.andExpect(jsonPath("$.bonus").value(5))
    		.andExpect(jsonPath("$.stat").value("ATQ"))
    		.andExpect(jsonPath("$.rarity").value("COMMON"));
    }
    
    // Cas : item introuvable
    @Test
    void getById_shouldReturn404_whenItemNotFound() throws Exception {
        // Arrange
        given(itemService.findById(itemId))
        	.willThrow(new ResourceNotFoundException("Item introuvable avec l'id : " + itemId));

        // Act / Assert
        mockMvc.perform(get("/items/{id}", itemId)
        		.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(status().isNotFound());
    }
    
    /* --------------------------------
       ------------ Create ------------ 
       -------------------------------- */
    
    // Cas : nom disponible
    @Test
    void create_shouldReturn201_whenItemIsCreated() throws Exception {
        // Arrange
        given(itemService.create(any(ItemRequestDTO.class))).willReturn(itemResponse);

        // Act / Assert
        mockMvc.perform(post("/items")
        		.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequest)))
        	.andExpect(status().isCreated())
        	.andExpect(jsonPath("$.id").value(itemId.toString()))
        	.andExpect(jsonPath("$.name").value("Épée rouillée"))
        	.andExpect(jsonPath("$.description").value("Une vieille épée"))
        	.andExpect(jsonPath("$.type").value("ONE_HAND_WEAPON"))
        	.andExpect(jsonPath("$.bonus").value(5))
        	.andExpect(jsonPath("$.stat").value("ATQ"))
        	.andExpect(jsonPath("$.rarity").value("COMMON"));
    }

    // Cas : nom déjà pris
    @Test
    void create_shouldReturn409_whenNameAlreadyExists() throws Exception {
        // Arrange
        given(itemService.create(any(ItemRequestDTO.class)))
        	.willThrow(new IllegalArgumentException("Un item avec le nom 'Épée rouillée' existe déjà"));

        // Act / Assert
        mockMvc.perform(post("/items")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(itemRequest)))
       		.andExpect(status().isConflict());
    }
    
    /* --------------------------------
       ------------ Update ------------ 
       -------------------------------- */
    
    // Cas : item trouvé + nom valide
    @Test
    void update_shouldReturn200_whenItemIsUpdated() throws Exception {
        // Arrange
        given(itemService.update(eq(itemId), any(ItemRequestDTO.class))).willReturn(itemResponse);

        // Act / Assert
        mockMvc.perform(put("/items/{id}", itemId)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(itemRequest)))
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$.id").value(itemId.toString()))
        	.andExpect(jsonPath("$.name").value("Épée rouillée"))
        	.andExpect(jsonPath("$.description").value("Une vieille épée"))
        	.andExpect(jsonPath("$.type").value("ONE_HAND_WEAPON"))
        	.andExpect(jsonPath("$.bonus").value(5))
        	.andExpect(jsonPath("$.stat").value("ATQ"))
        	.andExpect(jsonPath("$.rarity").value("COMMON"));
    }

    // Cas : item introuvable
    @Test
    void update_shouldReturn404_whenItemNotFound() throws Exception {
        // Arrange
        given(itemService.update(eq(itemId), any(ItemRequestDTO.class)))
        	.willThrow(new ResourceNotFoundException("Item introuvable avec l'id : " + itemId));

        // Act / Assert
        mockMvc.perform(put("/items/{id}", itemId)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(itemRequest)))
        	.andExpect(status().isNotFound());
    }
    
    // Cas : nom déjà pris
    @Test
    void update_shouldReturn409_whenNameAlreadyExists() throws Exception {
        // Given
        given(itemService.update(eq(itemId), any(ItemRequestDTO.class)))
        	.willThrow(new IllegalArgumentException("Un item avec le nom 'Épée rouillée' existe déjà"));

        // When / Then
        mockMvc.perform(put("/items/{id}", itemId)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(itemRequest)))
        	.andExpect(status().isConflict());
    }
    
    /* --------------------------------
       ------------ Delete ------------ 
       -------------------------------- */
    
    // Cas : item trouvé
    @Test
    void delete_shouldReturn204_whenItemIsDeleted() throws Exception {
        // Arrange
        willDoNothing().given(itemService).delete(itemId); // Pas de return => void

        // Act / Assert
        mockMvc.perform(delete("/items/{id}", itemId))
        	.andExpect(status().isNoContent());
    }

    // Cas : item introuvable
    @Test
    void delete_shouldReturn404_whenItemNotFound() throws Exception {
        // Arrange
        willThrow(new ResourceNotFoundException("Item introuvable avec l'id : " + itemId))
        	.given(itemService).delete(itemId);

        // Act / Assert
        mockMvc.perform(delete("/items/{id}", itemId))
        	.andExpect(status().isNotFound());
    }
}