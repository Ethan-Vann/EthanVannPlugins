package com.example.Toacito.AkkhaPath;

import com.example.Toacito.ToacitoConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

@Slf4j
public class AkkhaPathOverlay extends Overlay {
	@Inject
	private Client client;

	@Inject
	private AkkhaPath akkhaPath;

	@Inject
	protected ToacitoConfig toacitoConfig;

	@Inject
	AkkhaPathOverlay (Client client, AkkhaPath akkhaPath, ToacitoConfig config){
		this.akkhaPath=akkhaPath;
		this.client=client;
		this.toacitoConfig=config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(OverlayPriority.HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics) {
		if( this.akkhaPath.getContador() >=0 && this.toacitoConfig.obeliskConfig()){
			graphics.setFont(new Font("Arial",Font.BOLD,24));
			Point punto = Perspective.getCanvasTextLocation(client,graphics,akkhaPath.getObelisko().getLocalLocation(),String.valueOf(akkhaPath.getContador()),0);
			OverlayUtil.renderTextLocation(graphics,punto, String.valueOf(akkhaPath.getContador()), Color.MAGENTA);
		}

		return null;
	}


}
