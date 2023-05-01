package com.example.Toacito.wardenP34;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class WardenP3Overlay extends Overlay {
	@Inject
	private Client client;

	@Inject
	private WardenP3 wardenP3;
	private String lugar;

	@Inject
	WardenP3Overlay(Client client, WardenP3 wardenP3){
		this.client=client;
		this.wardenP3=wardenP3;
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics) {
		if(WardenP3.mostrar){
			if(WardenP3.safe==1){
				lugar="Izquierda";
			} else if (WardenP3.safe==2) {
				lugar="Centro";
			} else if (WardenP3.safe==3) {
				lugar="Derecha";
			}
			//le cambie el orden de set font
			graphics.setFont(new Font("Arial",Font.BOLD,24));
			Point pt = Perspective.getCanvasTextLocation(client,graphics,wardenP3.getWardencito().getLocalLocation(),lugar,0);
			OverlayUtil.renderTextLocation(graphics,pt,lugar, Color.ORANGE);
		}
		return null;
	}
}
