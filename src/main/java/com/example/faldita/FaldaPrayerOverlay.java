package com.example.faldita;

import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.VarClientInt;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
class FaldaPrayerOverlay extends Overlay {
	private static final Logger log = LoggerFactory.getLogger(FaldaPrayerOverlay.class);

	private final Client client;

	private final FaldaPlugin plugin;

	private final FaldaConfig config;

	@Inject
	private FaldaPrayerOverlay(Client client, FaldaPlugin plugin, FaldaConfig config) {
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.LOW);
	}

	public Dimension render(Graphics2D graphics) {
		if (!this.plugin.isInFight() || this.plugin.getNm() == null)
			return null;
		FaldaAttack attack = this.plugin.getPendingNightmareAttack();
		if (attack == null)
			return null;
		if (!this.config.prayerHelper().showWidgetHelper())
			return null;
		Color color = this.client.isPrayerActive(attack.getPrayer()) ? Color.GREEN : Color.RED;
		renderPrayerOverlay(graphics, this.client, attack.getPrayer(), color);
		return null;
	}

	public static Rectangle renderPrayerOverlay(Graphics2D graphics, Client client, Prayer prayer, Color color) {
		Widget wea = null;
		if(Prayer.PROTECT_FROM_MAGIC == prayer){
			wea = client.getWidget(35454997);
		} else if (Prayer.PROTECT_FROM_MISSILES == prayer) {
			wea = client.getWidget(35454998);
		} else if (Prayer.PROTECT_FROM_MELEE == prayer) {
			wea = client.getWidget(35454999);
		}else {
			wea = null;
		}
		//Widget widget = client.getWidget(prayer.getWidgetInfo());
		Widget widget = wea;
		//if (widget == null || client.getVar(171) != InterfaceTab.PRAYER.getId())
		if (widget == null || client.getVarcIntValue(VarClientInt.INVENTORY_TAB) != 5) //5 es la PRAYER_TAB
			return null;
		Rectangle bounds = widget.getBounds();
		OverlayUtil.renderPolygon(graphics, rectangleToPolygon(bounds), color);
		return bounds;
	}

	private static Polygon rectangleToPolygon(Rectangle rect) {
		int[] xpoints = { rect.x, rect.x + rect.width, rect.x + rect.width, rect.x };
		int[] ypoints = { rect.y, rect.y, rect.y + rect.height, rect.y + rect.height };
		return new Polygon(xpoints, ypoints, 4);
	}
}
