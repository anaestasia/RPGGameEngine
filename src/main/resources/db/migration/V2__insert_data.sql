-- ========================
-- ITEMS
-- ========================
INSERT INTO item (id, name, description, type, bonus, stat) VALUES
-- Helmets
(UUID_TO_BIN('00000000-0000-0000-0000-000000000001'), 'Choipeau',
 'Un chapeau léger qui ne protège que du soleil et qui ne décide de rien.',
 'HELMET', 5, 'DEF'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000002'), 'Casque Ade',
 'Un casque solide pour protéger des objets qui subissent la gravité.',
 'HELMET', 10, 'DEF'),

-- Armors
(UUID_TO_BIN('00000000-0000-0000-0000-000000000003'), 'Tenue d''Adam',
 'Une feuille de vigne qui protège votre intimité des voyeurs tout en profitant de la brise.',
 'ARMOR', 10, 'DEF'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000004'), 'Maillestro',
 'Une cote de maille pour avoir la classe des chevaliers et marcher en rythme.',
 'ARMOR', 20, 'DEF'),

-- One hand weapons
(UUID_TO_BIN('00000000-0000-0000-0000-000000000005'), 'Bâtronc',
 'Un bâton pour ceux qui veulent se la jouer Gandalf. Alors, ça passe ou pas ?',
 'ONE_HAND_WEAPON', 5, 'ATQ'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000006'), 'Coutopointu',
 'Une lame courte parce qu''on a toujours besoin de pouvoir découper un bon steack ou un ennemi.',
 'ONE_HAND_WEAPON', 10, 'ATQ'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000007'), 'Pan Taymore',
 'Un pistolet pour ceux qui préfèrent les menaces simples et efficaces.',
 'ONE_HAND_WEAPON', 15, 'ATQ'),

-- Two hand weapons
(UUID_TO_BIN('00000000-0000-0000-0000-000000000008'), 'Péri Gourdin',
 'Un gourdin qui peut taper de manière périphérique, ou écraser vos pommes de terre.',
 'TWO_HAND_WEAPON', 10, 'ATQ'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000009'), 'Lance Inante',
 'Une lance qui peut empaler des ennemis et provoque des douleurs intenses.',
 'TWO_HAND_WEAPON', 15, 'ATQ'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000010'), 'Lance Iraptor',
 'Une lance préhistorique acérée pour les fans du jurassik.',
 'TWO_HAND_WEAPON', 20, 'ATQ'),

-- Shields
(UUID_TO_BIN('00000000-0000-0000-0000-000000000011'), 'Ecu Villon',
 'Un bouclier plutôt fin qui protège qu''une seule partie du corps à la fois.',
 'SHIELD', 5, 'DEF'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000012'), 'Ecu Reuil',
 'Un bouclier de fourrure pour amortir le choc des projectiles.',
 'SHIELD', 10, 'DEF'),

-- Rings
(UUID_TO_BIN('00000000-0000-0000-0000-000000000013'), 'Ano Stekké',
 'Un anneau qui vous rend plus confiant, vous affrontez courageusement le danger avec un bonus de PV.',
 'RING', 5, 'PV'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000014'), 'Ano Rexique',
 'Un anneau qui vous rend plus fin et vous confère un bonus de défense car votre hitbox est plus petite.',
 'RING', 10, 'DEF'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000015'), 'Bague Arre',
 'Un anneau qui vous aidera à développer vos compétences de combat de rue et vous donne un bonus d''attaque.',
 'RING', 10, 'ATQ'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000016'), 'Bague Era',
 'Un anneau qui vous rend aussi agile qu''un félin et vous permet d''esquiver plus facilement.',
 'RING', 2, 'SPEED'),

-- Shoes
(UUID_TO_BIN('00000000-0000-0000-0000-000000000017'), 'Chausse Tentatwar',
 'Des chaussures qui claquent pour rendre jaloux vos ennemis mais ne permet pas de courir très vite.',
 'SHOE', 5, 'DEF'),
(UUID_TO_BIN('00000000-0000-0000-0000-000000000018'), 'Botte Eurfly',
 'Des chaussures légères avec une semelle irisée qui vous donnent l''impression de voler.',
 'SHOE', 10, 'DEF'),

-- Consumables
(UUID_TO_BIN('00000000-0000-0000-0000-000000000019'), 'Potion de vie',
 'C''est dégueu, mieux vaut pas savoir ce qu''il y a dedans. Mais ça vous sauvera sûrement la vie. Peut-être.',
 'CONSUMABLE', 20, 'PV');

-- ========================
-- HEROS
-- ========================
INSERT INTO hero (id, name, pv, atq, def, speed, exp, race, role) VALUES
(UUID_TO_BIN('00000000-0000-0000-0001-000000000001'), 'Narvatho',   10, 2, 0, 1, 0, 'HUMAN',  'THIEF'),
(UUID_TO_BIN('00000000-0000-0000-0001-000000000002'), 'SoForce',   10, 2, 0, 1, 0, 'ORC',    'MAGICIAN'),
(UUID_TO_BIN('00000000-0000-0000-0001-000000000003'), 'JohnDoe',    10, 2, 0, 1, 0, 'HUMAN',  'THIEF'),
(UUID_TO_BIN('00000000-0000-0000-0001-000000000004'), 'Moumoune59', 10, 2, 0, 1, 0, 'DWARF',  'WARRIOR'),
(UUID_TO_BIN('00000000-0000-0000-0001-000000000005'), 'Djangonnard',    10, 2, 0, 1, 0, 'ELF',    'BARD');

-- UUIDs de test sont volontairement lisibles pour facilite le debug
--		Les items terminent par 0000-000000000001
--		Les personnages commencent par 0001-000000000001