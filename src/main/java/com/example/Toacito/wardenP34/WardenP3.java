package com.example.Toacito.wardenP34;

import com.example.Toacito.Sala;
import com.example.Toacito.ToacitoConfig;
import com.example.Toacito.ToacitoPlugin;
import lombok.Getter;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;

@Getter
public class WardenP3 extends Sala {

	@Inject
	private Client client;

	@Inject
	private WardenP3Overlay wardenP3Overlay;

	@Inject
	private Pantheon pantheon;


	public static int safe=0;
	public static boolean mostrar=false;
	private Actor wardencito;

	@Inject
	protected WardenP3(ToacitoConfig config, ToacitoPlugin plugin) {
		super(config, plugin);
	}

	@Override
	public void load() {
		this.overlayManager.add(wardenP3Overlay);
		this.pantheon.cargar();
	}

	@Override
	public void unload() {
		this.overlayManager.remove(wardenP3Overlay);
		this.pantheon.descargar();
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event){
		if(!(event.getActor() instanceof NPC)) return;
		//NPC npc= (NPC) event.getActor(); //ver ID de Elidinis warden  (tumeken Warden=11762)
		this.pantheon.onAnimationChanged(event);
		//if(npc.getId()!= 11762) return;
		if(!config.floorPosition()) return;
		if(event.getActor().getName() == null) return;
		if(!event.getActor().getName().contains("Warden")){
			return;
		}
		if(event.getActor().getAnimation()==9675){
			if(wardencito==null){
				wardencito=event.getActor();
			}
			if(mostrar) mostrar = false;
			safe=1; //derecha
		} else if (event.getActor().getAnimation()==9677) {
			if(mostrar) mostrar = false;
			safe=2;	//izquierda
		} else if (event.getActor().getAnimation()==9679) {
			if(mostrar) mostrar = false;
			safe=3;	//centro
		} else if (event.getActor().getAnimation()==9682) {
			mostrar=true;
		}


	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event){
		this.pantheon.onNpcSpawned(event);
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event){
		this.pantheon.onNpcDespawned(event);
		if(wardencito!=null){
			if(event.getActor().equals(wardencito)){
				wardencito = null;
				mostrar=false;
			}
		}
	}
}
