package com.example.Toacito.wardenP12;

import com.example.Toacito.ToacitoConfig;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class CoreOverlay extends Overlay {
	@Inject
	private Client client;

	@Inject
	private ToacitoConfig toacitoConfig;

	@Inject
	private WardenP2 wardenP2;

	@Inject
	CoreOverlay(Client client, WardenP2 wardenP2){
		this.client=client;
		this.wardenP2=wardenP2;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.LOW);
	}

	@Override
	public Dimension render(Graphics2D graphics) {
		if(wardenP2.getHitCounter()>0 && wardenP2.getCorazon()!=null){
			graphics.setFont(new Font("Arial",Font.BOLD,24));
			Point pt= Perspective.getCanvasTextLocation(client,graphics, wardenP2.getCorazon().getLocalLocation(), String.valueOf(wardenP2.getHitCounter()),wardenP2.getCorazon().getLogicalHeight());
			OverlayUtil.renderTextLocation(graphics,pt,String.valueOf(wardenP2.getHitCounter()), Color.ORANGE);
		}
		return null;
	}
}
