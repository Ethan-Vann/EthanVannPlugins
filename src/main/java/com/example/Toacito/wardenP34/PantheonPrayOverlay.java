package com.example.Toacito.wardenP34;

import net.runelite.api.Client;
import net.runelite.api.VarClientInt;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class PantheonPrayOverlay extends Overlay {
	@Inject
	private Pantheon pantheon;

	@Inject
	private Client client;

	@Inject
	PantheonPrayOverlay(Client client, Pantheon pantheonPlugin){
		this.client=client;
		this.pantheon=pantheonPlugin;
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.HIGH);
		setPosition(OverlayPosition.DYNAMIC);
	}

	@Override
	public Dimension render(Graphics2D graphics) {
		if (pantheon.isOverlaycito())
		{
			Color colorzobich;
			Widget praycito = null;
			if (pantheon.isRanged()){
				colorzobich = Color.GREEN;
				praycito=client.getWidget(35454998);
			}else {
				colorzobich = Color.CYAN;
				praycito=client.getWidget(35454997);
			}
			Widget widget = praycito;
			if (widget == null || client.getVarcIntValue(VarClientInt.INVENTORY_TAB) != 5) //5 es la PRAYER_TAB
				return null;
			Rectangle limites = widget.getBounds();
			OverlayUtil.renderPolygon(graphics,rectangleToPolygon(limites),colorzobich);
		}
		return null;
	}

	private static Polygon rectangleToPolygon(Rectangle rect) {
		int[] xpoints = { rect.x, rect.x + rect.width, rect.x + rect.width, rect.x };
		int[] ypoints = { rect.y, rect.y, rect.y + rect.height, rect.y + rect.height };
		return new Polygon(xpoints, ypoints, 4);
	}
}
