package com.example.Toacito.wardenP34;

import com.example.Toacito.ToacitoConfig;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@Getter
public class Pantheon {
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PantheonOverlay pantheonOverlay;

	@Inject
	private PantheonPrayOverlay pantheonPrayOverlay;


	@Inject
	private ToacitoConfig config;

	@Inject
	Pantheon(ToacitoConfig config){
		this.config=config;
	}



	private boolean ranged = true;
	private boolean Overlaycito = false;
	private NPC maricon;





	public void cargar(){
		this.overlayManager.add(pantheonOverlay);
		this.overlayManager.add(pantheonPrayOverlay);
	}

	public void descargar(){
		this.overlayManager.remove(pantheonOverlay);
		this.overlayManager.remove(pantheonPrayOverlay);
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		if(event == null || !(event.getActor() instanceof NPC))
		{
			return;
		}

		if(((NPC) event.getActor()).getId()==11777){
			if (event.getActor().getAnimation() == 9777)
			{
				ranged = !ranged;
			}
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		if(event.getNpc() == null){
			return;
		}
		if(event.getNpc().getId() == 11777){
			maricon=event.getNpc();
			Overlaycito = !Overlaycito;
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if(event.getNpc() == null){
			return;
		}
		if(event.getNpc().getId() == 11777){
			maricon=null;
			Overlaycito = !Overlaycito;
			ranged = true;
		}
	}

}
