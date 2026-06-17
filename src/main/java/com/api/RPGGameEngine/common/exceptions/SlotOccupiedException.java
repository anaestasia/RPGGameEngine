package com.api.RPGGameEngine.common.exceptions;

import com.api.RPGGameEngine.common.enums.SlotType;

public class SlotOccupiedException extends RuntimeException {
	public SlotOccupiedException(SlotType slot) {
		super("Le slot " + slot + " est déjà occupé");
    }

}
