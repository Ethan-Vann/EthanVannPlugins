package com.example.Toacito.wardenP12;

import com.example.Toacito.ToacitoConfig;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class WardenP2Overlay extends Overlay {
	@Inject
	private Client client;

	@Inject
	private WardenP2 wardenP2;

	@Inject
	private ToacitoConfig toacitoConfig;

	@Inject
	WardenP2Overlay(Client client, WardenP2 wardenP2){
		this.client=client;
		this.wardenP2=wardenP2;
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics) { //ponerle condiciones
		if(WardenP2.enPelea && toacitoConfig.pikachusConfig()) {
			for (int a = 0; a < WardenP2.endPos.length; a++) {
				if (WardenP2.tickLeft[a] > 0) {
					Polygon poly = Perspective.getCanvasTilePoly(client, WardenP2.endPos[a]);
					/*
					graphics.setPaintMode();
					graphics.setColor(Color.RED);
					graphics.setStroke(new BasicStroke(2));
					graphics.draw(poly);
					 */
					Color colorcito = new Color(0,0,0,100);
					OverlayUtil.renderPolygon(graphics,poly,Color.RED,colorcito,new BasicStroke(2));
				}
			}
		}


		return null;
	}
}
