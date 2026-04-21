package com.api.RPGGameEngine.common.enums;

public enum HeroRole {
	THIEF(0, 0, 0, 1),
    BARD(0, 0, 2, 0),
    MAGICIAN(0, 3, 0, 0),
    WARRIOR(5, 0, 0, 0);

    private final int bonusPv;
    private final int bonusAtq;
    private final int bonusDef;
    private final int bonusSpeed;

    HeroRole(int bonusPv, int bonusAtq, int bonusDef, int bonusSpeed) {
        this.bonusPv    = bonusPv;
        this.bonusAtq   = bonusAtq;
        this.bonusDef   = bonusDef;
        this.bonusSpeed = bonusSpeed;
    }

    public int getBonusPv()    { return bonusPv; }
    public int getBonusAtq()   { return bonusAtq; }
    public int getBonusDef()   { return bonusDef; }
    public int getBonusSpeed() { return bonusSpeed; }
}
