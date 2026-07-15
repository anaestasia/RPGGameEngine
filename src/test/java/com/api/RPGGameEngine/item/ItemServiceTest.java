package com.api.RPGGameEngine.item;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.api.RPGGameEngine.common.enums.ItemRarity;
import com.api.RPGGameEngine.common.enums.ItemType;
import com.api.RPGGameEngine.common.enums.StatType;
import com.api.RPGGameEngine.common.exceptions.ResourceNotFoundException;
import com.api.RPGGameEngine.item.dto.ItemRequestDTO;
import com.api.RPGGameEngine.item.dto.ItemResponseDTO;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

	@Mock // Créé un faux objet qui ne fait rien par défaut
	private ItemRepository itemRepository;
	
	@InjectMocks // crée une vraie instance de la classe testée et y injecte automatiquement les @Mock déclarés au-dessus
	private ItemService itemService;
	
	private Item item;
    private UUID itemId;

    @BeforeEach // exécute setUp() avant chaque test, pour repartir sur une donnée propre
    void setUp() {
        itemId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        item = Item.builder()
                .id(itemId)
                .name("Épée rouillée")
                .description("Une vieille épée")
                .type(ItemType.ONE_HAND_WEAPON)
                .bonus(5)
                .stat(StatType.ATQ)
                .rarity(ItemRarity.COMMON)
                .build();
    }
    
    /* --------------------------------
       ----------- FindById ----------- 
       -------------------------------- */
    
    // Cas : item trouvé
    @Test
    void findById_shouldReturnItem_whenItemExists() {
        // Arrange : on dit au mock quoi renvoyer quand on l'appelle avec cet ID
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));

        // Act : on appelle la méthode du service qu'on teste
        ItemResponseDTO result = itemService.findById(itemId);

        // Assert : on vérifie le résultat
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Épée rouillée");
    }
    
    // Cas : item introuvable
    @Test
    void findById_shouldThrowException_whenItemNotFound() { 
        // Arrange
        given(itemRepository.findById(itemId)).willReturn(Optional.empty());

        // Act / Assert
        assertThatThrownBy(() -> itemService.findById(itemId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item introuvable avec l'id : " + itemId);

        // On vérifie qu'il n'y a pas eu de save()
        verify(itemRepository, never()).save(any());
    }
    
    /* --------------------------------
       ----------- Create -------------
       -------------------------------- */
    
    // Cas : nom disponible
    @Test
    void create_shouldReturnCreatedItem_whenNameisAvailable() {
    	
    	// Arrange
    	ItemRequestDTO dto = new ItemRequestDTO(
                item.getName(),
                item.getDescription(),
                item.getType(),
                item.getBonus(),
                item.getStat(),
                item.getRarity()
        );
    	given(itemRepository.existsByName(dto.name())).willReturn(false);
    	given(itemRepository.save(any(Item.class))).willReturn(item);
    	// any(Item.class) : Accepte n'importe quel item ("item" désigne déjà celui créé dans le SetUp())
    	
    	
    	// Act
    	ItemResponseDTO result = itemService.create(dto);
    	
    	// Assert
    	assertThat(result).isNotNull();
    	assertThat(result.name()).isEqualTo("Épée rouillée");
        verify(itemRepository).save(any(Item.class));
    }
    
    // Cas : nom déjà pris
    @Test
    void create_shouldThrowException_whenNameAlreadyExists() {
        // Arrange
        ItemRequestDTO dto = new ItemRequestDTO(
                "Epée neuve", // Appartient déjà à un autre item (pas besoin qu'il existe réellement en base)
                item.getDescription(),
                item.getType(),
                item.getBonus(),
                item.getStat(),
                item.getRarity()
        );
        given(itemRepository.existsByName(dto.name())).willReturn(true);

        // Act / Assert
        assertThatThrownBy(() -> itemService.create(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("existe déjà");

        // Le save ne doit jamais être atteint
        verify(itemRepository, never()).save(any());
    }
    
    /* --------------------------------
       ----------- Update -------------
       -------------------------------- */
    
    // Cas : nom disponible + item trouvé
    @Test
    void update_shouldReturnUpdatedItem_whenItemExistsAndNameIsAvailable() {
        // Arrange
        ItemRequestDTO dto = new ItemRequestDTO(
                "Épée neuve", // Nouveau nom
                "Une épée toute neuve", // Nouvelle description
                item.getType(),
                item.getBonus(),
                item.getStat(),
                item.getRarity()
        );
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));
        given(itemRepository.existsByName(dto.name())).willReturn(false);
        given(itemRepository.save(any(Item.class))).willReturn(item);

        // Act
        ItemResponseDTO result = itemService.update(itemId, dto);

        // Assert
        assertThat(result).isNotNull();
        verify(itemRepository).save(any(Item.class));
    }
    
    // Cas : item introuvable
    @Test
    void update_shouldThrowException_whenItemNotFound() {
        // Arrange
        ItemRequestDTO dto = new ItemRequestDTO(
                "Épée neuve",
                "Une épée toute neuve",
                item.getType(),
                item.getBonus(),
                item.getStat(),
                item.getRarity()
        );
        given(itemRepository.findById(itemId)).willReturn(Optional.empty());

        // Act / Assert
        assertThatThrownBy(() -> itemService.update(itemId, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item introuvable avec l'id");

        verify(itemRepository, never()).save(any());
    }
    
    // Cas : nom déjà pris
    @Test
    void update_shouldThrowException_whenNewNameAlreadyExists() {
        // Arrange
        ItemRequestDTO dto = new ItemRequestDTO(
        		"Épée existante", // Appartient déjà à un autre item
                "Une épée qui existe déjà",
                item.getType(),
                item.getBonus(),
                item.getStat(),
                item.getRarity()
        );
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));
        given(itemRepository.existsByName(dto.name())).willReturn(true);

        // Act / Assert
        assertThatThrownBy(() -> itemService.update(itemId, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("existe déjà");

        verify(itemRepository, never()).save(any());
    }
    
    /* --------------------------------
       ----------- Delete -------------
       -------------------------------- */
    
    // Cas : item trouvé
    @Test
    void delete_shouldDeleteItem_whenItemExists() {
        // Arrange
        given(itemRepository.existsById(itemId)).willReturn(true);

        // Act
        itemService.delete(itemId);

        // Assert — delete retourne void, donc on vérifie que deleteById a bien été appelé
        verify(itemRepository).deleteById(itemId);
    }
    
    
    // Cas : item introuvable
    @Test
    void delete_shouldThrowException_whenItemNotFound() {
        // Arrange
        given(itemRepository.existsById(itemId)).willReturn(false);

        // Act / Assert
        assertThatThrownBy(() -> itemService.delete(itemId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item introuvable avec l'id");

        verify(itemRepository, never()).deleteById(any());
    }
    
    /* ---------------------------------
       -------- FindByFilters ----------
       --------------------------------- */
    
    // Cas : aucun filtre
    @Test
    void findByFilters_shouldReturnAllItems_whenNoFilterProvided() {
    	// Arrange
        given(itemRepository.findAll()).willReturn(List.of(item));

        // Act
        List<ItemResponseDTO> result = itemService.findByFilters(null, null);

        // Assert
        assertThat(result).hasSize(1);
        
        // On vérifie qu'on rentre dans le bon cas du if/else
        verify(itemRepository).findAll();
        
        // On vérifie que les autres cas ne sont pas déclenchés
        verify(itemRepository, never()).findByType(any());
        verify(itemRepository, never()).findByRarity(any());
        verify(itemRepository, never()).findByTypeAndRarity(any(), any());
    }
    
    // Cas : type uniquement
    @Test
    void findByFilters_shouldReturnFilteredItems_whenOnlyTypeProvided() {
    	// Arrange
        given(itemRepository.findByType(ItemType.ARMOR)).willReturn(List.of(item));

        // Act
        List<ItemResponseDTO> result = itemService.findByFilters(ItemType.ARMOR, null);

        // Assert
        assertThat(result).hasSize(1);
        
        // On vérifie qu'on rentre dans le bon cas du if/else
        verify(itemRepository).findByType(ItemType.ARMOR);
        
        // On vérifie que les autres cas ne sont pas déclenchés
        verify(itemRepository, never()).findAll();
        verify(itemRepository, never()).findByRarity(any());
        verify(itemRepository, never()).findByTypeAndRarity(any(), any());
    }
    
    // Cas : rareté uniquement
    @Test
    void findByFilters_shouldReturnFilteredItems_whenOnlyRarityProvided() {
    	// Arrange
        given(itemRepository.findByRarity(ItemRarity.COMMON)).willReturn(List.of(item));

        // Act
        List<ItemResponseDTO> result = itemService.findByFilters(null, ItemRarity.COMMON);

        // Assert
        assertThat(result).hasSize(1);
        
        // On vérifie qu'on rentre dans le bon cas du if/else
        verify(itemRepository).findByRarity(ItemRarity.COMMON);
        
        // On vérifie que les autres cas ne sont pas déclenchés
        verify(itemRepository, never()).findAll();
        verify(itemRepository, never()).findByType(any());
        verify(itemRepository, never()).findByTypeAndRarity(any(), any());
    }
    
    // Cas : type + rareté
    @Test
    void findByFilters_shouldReturnFilteredItems_whenTypeAndRarityProvided() {
    	// Arrange
        given(itemRepository.findByTypeAndRarity(ItemType.ARMOR, ItemRarity.COMMON)).willReturn(List.of(item));

        // Act
        List<ItemResponseDTO> result = itemService.findByFilters(ItemType.ARMOR, ItemRarity.COMMON);

        // Assert
        assertThat(result).hasSize(1);
        
        // On vérifie qu'on rentre dans le bon cas du if/else
        verify(itemRepository).findByTypeAndRarity(ItemType.ARMOR, ItemRarity.COMMON);
        
        // On vérifie que les autres cas ne sont pas déclenchés
        verify(itemRepository, never()).findAll();
        verify(itemRepository, never()).findByType(any());
        verify(itemRepository, never()).findByRarity(any());
    }
    
    // Cas : aucun match avec les filtres
    @Test
    void findByFilters_shouldReturnEmptyList_whenNoItemMatchesFilters() {
        // Arrange
        given(itemRepository.findByTypeAndRarity(ItemType.ARMOR, ItemRarity.LEGENDARY)).willReturn(List.of());

        // Act
        List<ItemResponseDTO> result = itemService.findByFilters(ItemType.ARMOR, ItemRarity.LEGENDARY);

        // Assert
        assertThat(result).isNotNull();  // ne doit pas retourner null
        assertThat(result).isEmpty();    // mais une liste vide
    }
}
