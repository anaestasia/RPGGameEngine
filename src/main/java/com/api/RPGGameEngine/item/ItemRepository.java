package com.api.RPGGameEngine.item;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.RPGGameEngine.common.enums.ItemType;


// Les interfaces favorisent l'interchangeabilité des objets et la souplesse
// alors que les classes abstraites offrent une structure hiérarchisée et des fonctionnalités communes.

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
	List<Item> findByType(ItemType type);

    boolean existsByName(String name);
}
