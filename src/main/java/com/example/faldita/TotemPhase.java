package com.example.faldita;

import java.awt.*;

public enum TotemPhase {
	TOTEM_9434(false, Color.RED),
	TOTEM_9437(false, Color.RED),
	TOTEM_9440(false, Color.RED),
	TOTEM_9443(false, Color.RED),
	TOTEM_9435(true, Color.ORANGE),
	TOTEM_9438(true, Color.ORANGE),
	TOTEM_9441(true, Color.ORANGE),
	TOTEM_9444(true, Color.ORANGE),
	TOTEM_9436(true, Color.GREEN),
	TOTEM_9439(true, Color.GREEN),
	TOTEM_9442(true, Color.GREEN),
	TOTEM_9445(true, Color.GREEN);

	private final boolean active;

	private final Color color;

	TotemPhase(boolean active, Color color) {
		this.active = active;
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	public boolean isActive() {
		return this.active;
	}
}
