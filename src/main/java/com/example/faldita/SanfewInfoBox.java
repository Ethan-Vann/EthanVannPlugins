package com.example.faldita;

import net.runelite.api.Client;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.util.AsyncBufferedImage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;

@Singleton
public class SanfewInfoBox extends Overlay {
	private final Client client;

	private final FaldaPlugin plugin;

	private final FaldaConfig config;

	private final ItemManager itemManager;

	private final PanelComponent imagePanelComponent = new PanelComponent();

	@Inject
	private SanfewInfoBox(Client client, FaldaPlugin plugin, ItemManager itemManager, FaldaConfig config) {
		this.client = client;
		this.plugin = plugin;
		this.itemManager = itemManager;
		this.config = config;
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.HIGH);
		setPosition(OverlayPosition.BOTTOM_RIGHT);
	}

	public Dimension render(Graphics2D graphics) {
		this.imagePanelComponent.getChildren().clear();
		if (!this.plugin.isInFight() || !this.plugin.isParasite() || !this.config.sanfewReminder())
			return null;
		AsyncBufferedImage asyncBufferedImage = this.itemManager.getImage(10925);
		this.imagePanelComponent.setBackgroundColor(new Color(150, 0, 0, 150));
		this.imagePanelComponent.getChildren().add(new ImageComponent((BufferedImage)asyncBufferedImage));
		return this.imagePanelComponent.render(graphics);
	}
}
