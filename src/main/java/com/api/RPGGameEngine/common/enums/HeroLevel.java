package com.api.RPGGameEngine.common.enums;

import java.util.Arrays;

public enum HeroLevel {
	//LEVEL (niveau, exp nécessaire)
    LEVEL_1 (1,    0),
    LEVEL_2 (2,  100),
    LEVEL_3 (3,  250),
    LEVEL_4 (4,  500),
    LEVEL_5 (5,  900),
    LEVEL_6 (6, 1400),
    LEVEL_7 (7, 2000),
    LEVEL_8 (8, 2800),
    LEVEL_9 (9, 3800),
    LEVEL_10(10, 5000);

    private final int level;
    private final int expRequired;

    HeroLevel(int level, int expRequired) {
        this.level = level;
        this.expRequired = expRequired;
    }

    public int getLevel() { return level; }
    public int getExpRequired() { return expRequired; }

    /**
     * Calcule le niveau actuel en fonction des points d'XP.
     * Retourne le level le plus élevé dont le seuil est atteint.
     */
    public static HeroLevel fromExp(int exp) {
        return Arrays.stream(values())
                .filter(l -> exp >= l.expRequired)
                .reduce((first, second) -> second)
                .orElse(LEVEL_1);
    }

    /**
     * Retourne l'XP manquante pour atteindre le niveau suivant.
     * Retourne 0 si niveau maximum atteint.
     */
    public int expToNextLevel(int currentExp) {
        HeroLevel next = next();
        return next != null ? next.expRequired - currentExp : 0;
    }

    /**
     * Retourne le niveau suivant, ou null si niveau maximum.
     */
    public HeroLevel next() {
        int nextOrdinal = this.ordinal() + 1;
        HeroLevel[] levels = values();
        return nextOrdinal < levels.length ? levels[nextOrdinal] : null;
    }
}