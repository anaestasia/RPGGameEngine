package com.api.RPGGameEngine.item;

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
    
    @Test
    void findById_shouldReturnItem_whenItemExists() {
        // Given : on dit au mock quoi renvoyer quand on l'appelle avec cet ID
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));

        // When : on appelle la méthode du service qu'on teste
        ItemResponseDTO result = itemService.findById(itemId);

        // Then : on vérifie le résultat
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Épée rouillée");
    }
    
    @Test
    void findById_shouldThrowException_whenItemNotFound() { 
        // Given
        given(itemRepository.findById(itemId)).willReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> itemService.findById(itemId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item introuvable avec l'id");

        // On vérifie qu'il n'y a pas eu de save()
        verify(itemRepository, never()).save(any());
    }
}
