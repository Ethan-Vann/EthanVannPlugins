package com.example.Toacito.akkha;

import com.example.Toacito.Sala;
import com.example.Toacito.ToacitoConfig;
import com.example.Toacito.ToacitoPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Akkha extends Sala {
	@Inject
	private Client client;
	private boolean captura;

	@Inject
	protected Akkha(ToacitoConfig config, ToacitoPlugin plugin) {
		super(config, plugin);
	}

	private final Map<Integer, NPC> orbes = new HashMap<>();

	@Subscribe
	public void onNpcSpawned(NpcSpawned event){

		if(event.getNpc().getId()==11804){
			NPC orbe= event.getNpc();
			orbes.putIfAbsent(orbe.getIndex(),orbe);

		}

	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event){
		/*
		if(event.getNpc().getId()==AKKHA){
			captura =true;
		}

		 */
		if(event.getNpc().getId()==11804){
			NPC orbe = event.getNpc();
			orbes.remove(orbe.getIndex());
			orbe.getWorldLocation();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event){
		if(captura){

		}
	}
}
