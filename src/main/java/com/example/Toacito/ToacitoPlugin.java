package com.example.Toacito;

import com.example.Toacito.AkkhaPath.AkkhaPath;
import com.example.Toacito.baba.Baba;
import com.example.Toacito.lobby.Lobby;
import com.example.Toacito.wardenP12.WardenP2;
import com.example.Toacito.wardenP34.WardenP3;
import com.example.Toacito.wardenP34.WardenP4;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(name = "<html><font color=\"#ff6961\">Chupalo Guru</font></html>", description = "weas de toa",tags = {"pajau","toa"})
public class ToacitoPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ToacitoConfig config;

	@Inject
	private AkkhaPath akkhaPath;

	@Inject
	private Baba baba;

	@Inject
	private WardenP2 wardenP2;

	@Inject
	private WardenP3 wardenP3;

	@Inject
	private WardenP4 wardenP4;

	@Inject
	private Lobby lobby;

	private Sala[] rooms = null;



	public static final int AKKHA_PATH_REGION = 14674;
	public static final int BABA_REGION = 15188;
	//agregar region de wardens p2 y p3
	public static final int WARDEN_P12_REGION = 15184;

	@Provides
	ToacitoConfig getConfig(ConfigManager configManager) {
		return (ToacitoConfig) configManager.getConfig(ToacitoConfig.class);
	}

	@Override
	protected void startUp(){
		if (this.rooms==null){
			this.rooms=new Sala[]{ (Sala) this.akkhaPath, (Sala) this.baba,(Sala) this.wardenP2,(Sala) this.wardenP3,(Sala) this.wardenP4};

			for(Sala wea: this.rooms){
				wea.init();
				wea.load();
			}
		}
	}

	@Override
	protected void shutDown(){
		for(Sala wea: this.rooms){
			wea.unload();
		}
	}

	@Subscribe
	void onGameStateChanged(GameStateChanged event){
		this.wardenP2.onGameStateChanged(event);
		this.lobby.onGameStateChanged(event);
	}



	@Subscribe
	void onNpcSpawned(NpcSpawned event){
		this.akkhaPath.onNpcSpawned(event);
		this.baba.onNpcSpawned(event);
		this.wardenP3.onNpcSpawned(event);
		this.wardenP2.onNpcSpawned(event);

	}

	@Subscribe
	void onNpcDespawned(NpcDespawned event){
		this.wardenP3.onNpcDespawned(event);
		this.akkhaPath.onNpcDespawned(event);
		this.wardenP2.onNpcDespawned(event);
	}

	@Subscribe
	void onGameTick(GameTick event)throws Exception{
		this.akkhaPath.onGameTick(event);
		this.baba.onGameTick(event);
		this.wardenP2.onGameTick(event);
		this.lobby.onGameTick(event);
	}
	@Subscribe
	void onGraphicsObjectCreated(GraphicsObjectCreated event){
		this.akkhaPath.onGraphicsObjectCreated(event);
		this.wardenP4.onGraphicsObjectCreated(event);
	}

	@Subscribe
	void onAnimationChanged(AnimationChanged event){
		this.baba.onAnimationChanged(event);
		this.wardenP3.onAnimationChanged(event);
		this.wardenP2.onAnimationChanged(event);
	}

	@Subscribe
	void onProjectileMoved(ProjectileMoved event){
		this.wardenP2.onProjectileMoved(event);
	}

	@Subscribe
	void onGameObjectSpawned(GameObjectSpawned event){
		this.lobby.onGameObjectSpawned(event);
	}



}
