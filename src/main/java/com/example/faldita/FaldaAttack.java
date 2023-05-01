package com.example.faldita;

import net.runelite.api.Prayer;

import java.awt.*;

public enum FaldaAttack {
	MELEE(8594, Prayer.PROTECT_FROM_MELEE, Color.RED, 129),
	MAGIC(8595, Prayer.PROTECT_FROM_MAGIC, Color.CYAN, 127),
	RANGE(8596, Prayer.PROTECT_FROM_MISSILES, Color.GREEN, 128),
	CURSE_MELEE(8594, Prayer.PROTECT_FROM_MISSILES, Color.GREEN, 128),
	CURSE_MAGIC(8595, Prayer.PROTECT_FROM_MELEE, Color.RED, 129),
	CURSE_RANGE(8596, Prayer.PROTECT_FROM_MAGIC, Color.CYAN, 127);

	private final int animation;

	private final Prayer prayer;

	private final Color tickColor;

	private final int prayerSpriteId;

	FaldaAttack(int animation, Prayer prayer, Color tickColor, int prayerSpriteId) {
		this.animation = animation;
		this.prayer = prayer;
		this.tickColor = tickColor;
		this.prayerSpriteId = prayerSpriteId;
	}

	public int getAnimation() {
		return this.animation;
	}

	public Prayer getPrayer() {
		return this.prayer;
	}

	public Color getTickColor() {
		return this.tickColor;
	}

	public int getPrayerSpriteId() {
		return this.prayerSpriteId;
	}
}
