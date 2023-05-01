package com.example.Toacito.lobby;

import com.example.Toacito.Sala;
import com.example.Toacito.ToacitoConfig;
import com.example.Toacito.ToacitoPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.Arrays;

@Slf4j
public class Lobby extends Sala {
	@Inject
	private Client client;
	private boolean enLobby;
	private int wardens;
	private int akka;
	private int zebak;
	private int kephri;
	private int monito;

	@Inject
	protected Lobby(ToacitoConfig config, ToacitoPlugin plugin) {
		super(config, plugin);
	}

	private void resetear() {
		wardens=0;
		akka=0;
		zebak=0;
		kephri=0;
		monito=0;

	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event){
		//if(!config.suppPack1Config()) return;
		if(event.getGameState()== GameState.LOADING){
			int[] regiones = client.getMapRegions();
			enLobby= Arrays.stream(regiones).anyMatch(wea ->(wea==14160));
			//if(enLobby) log.info("Katarina: w{} ,a{} ,z{} ,k{} ,m{} ,enLobby={}",wardens,akka,zebak,kephri,monito,enLobby);
			if(!enLobby) resetear();
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event){
		if(!enLobby) return;
		if (event.getGameObject() == null) {
			return;
		}

		if(event.getGameObject().getId()==46168){
			wardens=1;
		} else if (event.getGameObject().getId()==46166) {
			akka=1;
		} else if (event.getGameObject().getId()==46163) {
			zebak=1;
		} else if (event.getGameObject().getId()==46157) {
			kephri=1;
		} else if (event.getGameObject().getId()==46160) {
			monito = 1;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event){
		//meow++;
		//if(meow%5==4) log.info("w{} ,a{} ,z{} ,k{} ,m{} ,enLobby={}",wardens,akka,zebak,kephri,monito,enLobby);
		if(enLobby) {
			if (kephri + akka + zebak + monito == 2) {
				if (client.getWidget(50921478) != null) {
					client.getWidget(50921478).setHidden(config.life1Config());
				}
				if (client.getWidget(777,9) != null) {
					client.getWidget(777,9).setHidden(config.chaos1Config());
				}
				if (client.getWidget(777,12) != null) {
					client.getWidget(777,12).setHidden(config.power1Config());
				}


			} else if (kephri +akka+zebak+monito==4) {
				if (client.getWidget(50921478) != null) {
					client.getWidget(50921478).setHidden(config.life2Config());
				}
				if (client.getWidget(777,9) != null) {
					client.getWidget(777,9).setHidden(config.chaos2Config());
				}
				if (client.getWidget(777,12) != null) {
					client.getWidget(777,12).setHidden(config.power2Config());
				}
			}
		}
	}




}
