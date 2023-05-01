package com.example.Toacito;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.ArrayUtils;

import javax.inject.Inject;

@Getter
public abstract class Sala {

	protected final ToacitoConfig config;
	protected final ToacitoPlugin plugin;

	@Inject
	protected OverlayManager overlayManager;

	@Inject
	private Client client;

	@Inject
	protected Sala(ToacitoConfig config, ToacitoPlugin plugin) {
		this.config = config;
		this.plugin = plugin;
	}

	public void init(){}
	public void load(){}
	public void unload(){}

	public boolean isRoomRegion(Integer roomId){
		return ArrayUtils.contains(this.client.getMapRegions(),roomId.intValue());
	}
}
