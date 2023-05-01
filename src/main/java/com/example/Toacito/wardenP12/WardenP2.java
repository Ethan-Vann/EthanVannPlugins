package com.example.Toacito.wardenP12;

import com.example.Toacito.Sala;
import com.example.Toacito.ToacitoConfig;
import com.example.Toacito.ToacitoPlugin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.Arrays;

@Getter
@Slf4j
public class WardenP2 extends Sala {

	@Inject
	private Client client;

	@Inject
	private ToacitoConfig toacitoConfig;

	@Inject
	private WardenP2Overlay wardenP2Overlay;

	@Inject
	private CoreOverlay coreOverlay;

	public static boolean enPelea= false;
	public static boolean inFight = false;
	private int hitCounter;
	private Actor corazon;


	@Override
	public void load() {
		this.overlayManager.add(wardenP2Overlay);
		this.overlayManager.add(coreOverlay);
	}

	@Override
	public void unload() {
		this.overlayManager.remove(wardenP2Overlay);
		this.overlayManager.remove(coreOverlay);
	}

	public static LocalPoint[] endPos = new LocalPoint[10];
	public static int[] tickLeft = new int[10];

	private static int nPick=0;

	private final int duracion = 5;


	@Inject
	protected WardenP2(ToacitoConfig config, ToacitoPlugin plugin) {
		super(config, plugin);
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event){
		if(event.getActor().getName() == null) return;
		if( event.getActor().getName().equals(client.getLocalPlayer().getName()) ){
			Actor yo = event.getActor();
			if(yo.getInteracting()==null) return;
			if(yo.getInteracting().getName()==null) return;
			if(yo.getInteracting().getName().toLowerCase().contains("core") && Arrays.stream(new int[]{829,-1,388}).noneMatch(w->(yo.getAnimation()==w))){
				corazon=yo.getInteracting();
				hitCounter++;
			}
		}
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved event){
		if(event.getProjectile()==null){
			return;
		}

		if(event.getProjectile().getId()==2225){//2225
			//log.info("Se agrego un Pikashu");
			endPos[nPick] = event.getProjectile().getTarget();
			tickLeft[nPick] = duracion;
			nPick++;
			if(nPick==tickLeft.length-1){
				nPick=0;
			}
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event){
		if(event.getNpc() == null) return;
		if(event.getNpc().getId() == 11750) {
			enPelea = true;
		}
	}


	@Subscribe
	public void onNpcDespawned(NpcDespawned event){
		if(event.getNpc() == null) return;

		if(event.getNpc().getId() == 11771) {
			hitCounter=0;
		}


	}


	@Subscribe
	public void onGameStateChanged(GameStateChanged event){
		if(event.getGameState() == GameState.LOADING){
			enPelea=false;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)throws Exception{
		//this.conta++;
		//if((this.conta)%5==4) log.info("conta: {}, pelea = {}",this.conta, enPelea);
		if(toacitoConfig.pikachusConfig() && enPelea){//npcSpawned warden - && isRoomRegion(WARDEN_P12_REGION)
			for(int c=0; c<tickLeft.length; c++){
				if (tickLeft[c] > 0) {
					//log.info("diminuyo");
					tickLeft[c]--;
				}
			}
		}
	}
}
