package com.example.Toacito.AkkhaPath;

import com.example.Toacito.Sala;
import com.example.Toacito.ToacitoConfig;
import com.example.Toacito.ToacitoPlugin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;

@Slf4j
@Getter
public class AkkhaPath extends Sala {

	@Inject
	private Client client;

	@Inject
	private ToacitoPlugin p;

	@Inject
	private AkkhaPathOverlay akkhaPathOverlay;

	private boolean aparecio;
	private int contador=-1;
	private NPC obelisko;

	@Inject
	protected AkkhaPath(ToacitoConfig config, ToacitoPlugin plugin) {
		super(config, plugin);
	}

	@Override
	public void load() {
		this.overlayManager.add(akkhaPathOverlay);
	}

	@Override
	public void unload() {
		this.overlayManager.remove(akkhaPathOverlay);
	}

	@Subscribe
	public void onGameTick(GameTick event){
		if(contador>=0) {
			contador--;
		}
	}

	@Subscribe
	public void onGraphicsObjectCreated(GraphicsObjectCreated event){
		if (event.getGraphicsObject() == null || obelisko==null) {
			return;
		}

		if ((event.getGraphicsObject().getId()==2114 ||
				event.getGraphicsObject().getId()==2119 ||
				event.getGraphicsObject().getId()==2064 ||
				event.getGraphicsObject().getId()==2120) &&
				contador != 9){

			contador=9;
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event){
		if(event.getNpc()==null) return;
		//if(!isRoomRegion(ToacitoPlugin.AKKHA_PATH_REGION)) return;
		if(event.getNpc().getId()==11706){
			obelisko=event.getNpc();
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event){
		if(event.getNpc().getId()==11706){
			obelisko=null;
		}
	}


}
