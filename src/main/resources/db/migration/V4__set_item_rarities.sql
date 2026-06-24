-- Update item rarities
UPDATE item SET rarity = 'COMMON'    WHERE name IN ('Choipeau','Tenue d''Adam', 'Bâtronc', 'Péri-Gourdin', 'Ecu Pidon', 'Ecu Reuil', 'Ano Stekké', 'Chausse Tentatwar', 'Jus de Survie');
UPDATE item SET rarity = 'RARE'      WHERE name IN ('Casque Hade', 'Maillestro', 'Coutopointu', 'Lance Ynante', 'Pare-Apluie', 'Ano Rexy', 'Ano Maly', 'Botte Eurfly', 'Élixir Respirefort');
UPDATE item SET rarity = 'EPIC'      WHERE name IN ('Heaumlette du Destin', 'Armuricaine', 'Flingandalf', 'Thorgnole', 'Remparfait', 'Bague Harre', 'Escarpin Gouins', 'Philtre Miraculix');
UPDATE item SET rarity = 'LEGENDARY' WHERE name IN ('Crâne d''Orduron', 'Carapax Ultime', 'Excalibourin', 'Masse Todontes', 'Bouclier Fiscal', 'Bague Era', 'Bottes de Sept Flemmes', 'Grand Cru de Jouvence');