package com.api.RPGGameEngine.item.dto;

import com.api.RPGGameEngine.common.enums.ItemRarity;
import com.api.RPGGameEngine.common.enums.ItemType;
import com.api.RPGGameEngine.common.enums.StatType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


// Record (java 16) : classe immubale pour transporter des données
// 		Réduit le boilerplate (constructeur, getter / setter, méthodes de base : equals, hashcode, toString)
// 		Améliore la lisibilité
// 		Champ final et private par défaut
// 		Le constructeur canonique garantit que tous les champs sont initialisés
// 		Pas de setters : l'objet est immuable après création => évite les effets de bord
// 		Méthodes utilitaires intégrées
// 		Accesseurs : name() au lieu de getName()

public record ItemRequestDTO(
		@NotBlank(message = "Le nom est obligatoire")
        String name,

        String description,

        @NotNull(message = "Le type est obligatoire")
        ItemType type,

        @Min(value = 1, message = "Le bonus doit être d'au moins 1")
        int bonus,

        @NotNull(message = "La statistique bonifiée est obligatoire")
        StatType stat,
        
        @NotNull(message = "La rareté est obligatoire")
        ItemRarity rarity
) {}
