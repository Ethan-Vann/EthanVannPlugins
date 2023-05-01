package com.example.faldita;

import net.runelite.api.NPC;

class MemorizedTotem {
	private NPC npc;

	private TotemPhase currentPhase;

	NPC getNpc() {
		return this.npc;
	}

	TotemPhase getCurrentPhase() {
		return this.currentPhase;
	}

	MemorizedTotem(NPC npc) {
		this.npc = npc;
		updateCurrentPhase(npc.getId());
	}

	public void updateCurrentPhase(int newId) {
		this.currentPhase = TotemPhase.valueOf("TOTEM_" + newId);
	}
}
