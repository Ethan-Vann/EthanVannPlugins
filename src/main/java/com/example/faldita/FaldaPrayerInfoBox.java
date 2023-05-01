package com.example.faldita;

import net.runelite.api.Client;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;

@Singleton
public class FaldaPrayerInfoBox extends Overlay {
	private static final Color NOT_ACTIVATED_BACKGROUND_COLOR = new Color(150, 0, 0, 150);

	private final Client client;

	private final FaldaPlugin plugin;

	private final FaldaConfig config;

	private final SpriteManager spriteManager;

	private final PanelComponent imagePanelComponent = new PanelComponent();

	@Inject
	private FaldaPrayerInfoBox(Client client, FaldaPlugin plugin, SpriteManager spriteManager, FaldaConfig config) {
		this.client = client;
		this.plugin = plugin;
		this.spriteManager = spriteManager;
		this.config = config;
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.HIGH);
		setPosition(OverlayPosition.BOTTOM_RIGHT);
	}

	public Dimension render(Graphics2D graphics) {
		this.imagePanelComponent.getChildren().clear();
		if (!this.plugin.isInFight() || this.plugin.getNm() == null)
			return null;
		FaldaAttack attack = this.plugin.getPendingNightmareAttack();
		if (attack == null)
			return null;
		if (!this.config.prayerHelper().showInfoBox())
			return null;
		BufferedImage prayerImage = getPrayerImage(attack);
		this.imagePanelComponent.setBackgroundColor(this.client.isPrayerActive(attack.getPrayer()) ? ComponentConstants.STANDARD_BACKGROUND_COLOR : NOT_ACTIVATED_BACKGROUND_COLOR);
		this.imagePanelComponent.getChildren().add(new ImageComponent(prayerImage));
		return this.imagePanelComponent.render(graphics);
	}

	private BufferedImage getPrayerImage(FaldaAttack attack) {
		return this.spriteManager.getSprite(attack.getPrayerSpriteId(), 0);
	}
}
