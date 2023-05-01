package com.example.Toacito.baba;

import com.example.Toacito.Sala;
import com.example.Toacito.ToacitoConfig;
import com.example.Toacito.ToacitoPlugin;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@Getter
public class Baba extends Sala {
	@Inject
	private Client client;

	@Inject
	private BabaOverlay babaOverlay;

	@Inject
	private OverlayManager overlayManager;
	private int count;
	private NPC mona;

	@Inject
	protected Baba(ToacitoConfig config, ToacitoPlugin plugin) {
		super(config, plugin);
	}


	@Override
	public void load() {
		this.overlayManager.add(babaOverlay);
	}

	@Override
	public void unload() {
		this.overlayManager.remove(babaOverlay);
	}

	//9743
	@Subscribe
	public void onGameTick(GameTick event){
		if (count>=0){
			count--;
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event){
		if(event.getActor()!=null){
			if(event.getActor().getAnimation()==9743){
				count=6;
			}
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event){
		if(event.getNpc()!=null){
			if(event.getNpc().getId()==11778){
				mona=event.getNpc();
			}
		}
	}
}
