package com.example.faldita;

import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.geometry.Geometry;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.Map;

@Singleton
class FaldaOverlay extends Overlay {
	private static final Logger log = LoggerFactory.getLogger(FaldaOverlay.class);

	private final Client client;

	private final FaldaPlugin plugin;

	private final FaldaConfig config;

	private final ModelOutlineRenderer outliner;

	private int timeout;

	private static final int NIGHTMARE_SHADOW = 1767;

	@Inject
	private FaldaOverlay(Client client, FaldaPlugin plugin, FaldaConfig config, ModelOutlineRenderer outliner) {
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.outliner = outliner;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(OverlayPriority.LOW);
	}

	public Dimension render(Graphics2D graphics) {
		if (!this.client.isInInstancedRegion() || !this.plugin.isInFight())
			return null;
		if (this.config.highlightShadows()) {
			for (GraphicsObject graphicsObject : this.plugin.getShadows()) {
				LocalPoint lp = graphicsObject.getLocation();
				Polygon poly = Perspective.getCanvasTilePoly(this.client, lp);
				Player localPlayer = this.client.getLocalPlayer();
				if (poly != null && localPlayer != null) {
					WorldPoint playerWorldPoint = localPlayer.getWorldLocation();
					WorldPoint shadowsWorldPoint = WorldPoint.fromLocal(this.client, lp);
					if (playerWorldPoint.distanceTo(shadowsWorldPoint) <= this.config.shadowsRenderDistance()) {
						graphics.setPaintMode();
						graphics.setColor(this.config.shadowsBorderColour());
						graphics.draw(poly);
						graphics.setColor(this.config.shadowsColour());
						graphics.fill(poly);
						if (this.config.shadowsTickCounter()) {
							String count = Integer.toString(this.plugin.getShadowsTicks());
							Point point = Perspective.getCanvasTextLocation(this.client, graphics, lp, count, 0);
							if (point != null)
								renderTextLocation(graphics, count, 12, 1, Color.WHITE, point);
						}
					}
				}
			}
			if (this.plugin.isShadowsSpawning() && this.plugin.getNm() != null) {
				Polygon tilePoly = Perspective.getCanvasTileAreaPoly(this.client, this.plugin.getNm().getLocalLocation(), 5);
				OverlayUtil.renderPolygon(graphics, tilePoly, this.config.shadowsBorderColour());
			}
		}
		if (this.config.highlightNightmareHitboxOnCharge() && this.plugin.getNm() != null)
			drawNightmareHitboxOnCharge(graphics, this.plugin.getNm(), this.plugin.isNightmareCharging());
		if (this.config.highlightNightmareChargeRange() && this.plugin.getNm() != null)
			drawNightmareChargeRange(graphics, this.plugin.getNm(), this.plugin.isNightmareCharging());
		int ticksUntilNext = this.plugin.getTicksUntilNextAttack();
		if (this.config.ticksCounter() && ticksUntilNext > 0 && this.plugin.getNm() != null) {
			String str = Integer.toString(ticksUntilNext);
			LocalPoint lp = this.plugin.getNm().getLocalLocation();
			Point point = Perspective.getCanvasTextLocation(this.client, graphics, lp, str, 0);
			Color tickColor = Color.WHITE;
			FaldaAttack nextAttack = this.plugin.getPendingNightmareAttack();
			if (ticksUntilNext >= 4 && nextAttack != null)
				tickColor = nextAttack.getTickColor();
			renderTextLocation(graphics, str, 20, 1, tickColor, point);
		}
		int ticksUntilNextParasite = this.plugin.getTicksUntilParasite();
		if (this.config.showTicksUntilParasite() && ticksUntilNextParasite > 0) {
			String str = Integer.toString(ticksUntilNextParasite);
			for (Player player : this.plugin.getParasiteTargets().values()) {
				LocalPoint lp = player.getLocalLocation();
				Point point = Perspective.getCanvasTextLocation(this.client, graphics, lp, str, 0);
				Color color = (!this.plugin.isParasite() && player.equals(this.client.getLocalPlayer())) ? Color.GREEN : Color.RED;
				renderTextLocation(graphics, str, 14, 1, color, point);
			}
		}
		if (this.config.highlightTotems())
			for (MemorizedTotem totem : this.plugin.getTotems().values()) {
				if (totem.getCurrentPhase().isActive())
					this.outliner.drawOutline(totem.getNpc(), this.config.totemOutlineSize(), totem.getCurrentPhase().getColor(), 0);
			}
		if (this.config.highlightSpores())
			drawPoisonArea(graphics, this.plugin.getSpores());
		if (this.config.highlightHuskTarget())
			drawHuskTarget(graphics, this.plugin.getHuskTarget());
		if (this.config.huskHighlight())
			renderHuskHighlights(graphics);
		if (this.plugin.isFlash() && this.config.flash()) {
			Color flash = graphics.getColor();
			graphics.setColor(new Color(255, 0, 0, 70));
			graphics.fill(new Rectangle(this.client.getCanvas().getSize()));
			graphics.setColor(flash);
			this.timeout++;
			if (this.timeout >= 50) {
				this.timeout = 0;
				this.plugin.setFlash(false);
			}
		}
		return null;
	}

	protected void renderTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, Point canvasPoint) {
		graphics.setFont(new Font("Arial", fontStyle, fontSize));
		if (canvasPoint != null) {
			Point canvasCenterPoint = new Point(canvasPoint.getX(), canvasPoint.getY());
			Point canvasCenterPointShadow = new Point(canvasPoint.getX() + 1, canvasPoint.getY() + 1);
			OverlayUtil.renderTextLocation(graphics, canvasCenterPointShadow, txtString, Color.BLACK);
			OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
		}
	}

	private void renderNpcOverlay(Graphics2D graphics, NPC actor, Color color) {
		Shape objectClickbox = actor.getConvexHull();
		graphics.setColor(color);
		graphics.draw(objectClickbox);
	}

	private void drawPoisonArea(Graphics2D graphics, Map<LocalPoint, GameObject> spores) {
		if (spores.size() < 1)
			return;
		Area poisonTiles = new Area();
		for (Map.Entry<LocalPoint, GameObject> entry : spores.entrySet()) {
			LocalPoint point = entry.getKey();
			Polygon poly = Perspective.getCanvasTileAreaPoly(this.client, point, 3);
			if (poly != null)
				poisonTiles.add(new Area(poly));
		}
		graphics.setPaintMode();
		graphics.setColor(this.config.poisonBorderCol());
		graphics.draw(poisonTiles);
		graphics.setColor(this.config.poisonCol());
		graphics.fill(poisonTiles);
	}

	private void drawNightmareHitboxOnCharge(Graphics2D graphics, NPC nm, boolean isNmCharging) {
		if (!isNmCharging)
			return;
		if (this.plugin.getNm() != null) {
			Polygon tilePoly = Perspective.getCanvasTileAreaPoly(this.client, this.plugin.getNm().getLocalLocation(), 5);
			OverlayUtil.renderPolygon(graphics, tilePoly, this.config.nightmareChargeBorderCol());
		}
	}

	private void drawNightmareChargeRange(Graphics2D graphics, NPC nm, boolean isNmCharging) {
		if (!isNmCharging)
			return;
		LocalPoint nmLocalPoint = nm.getLocalLocation();
		int nmX = nmLocalPoint.getX();
		int nmY = nmLocalPoint.getY();
		Area areaAddPoints = new Area();
		Polygon polyAddPoints = new Polygon();
		int offset = 1792;
		if (nmX == 6208 || nmX == 7232)
			offset = 2048;
		if (nmX == 5312 || nmX == 6336) {
			polyAddPoints.addPoint(nmX + offset + 256 + 64, nmY + 256 + 64);
			polyAddPoints.addPoint(nmX - 256 - 64, nmY + 256 + 64);
			polyAddPoints.addPoint(nmX - 256 - 64, nmY - 256 - 64);
			polyAddPoints.addPoint(nmX + offset + 256 + 64, nmY - 256 - 64);
		} else if (nmX == 7104 || nmX == 8128) {
			polyAddPoints.addPoint(nmX + 256 + 64, nmY + 256 + 64);
			polyAddPoints.addPoint(nmX - offset - 256 - 64, nmY + 256 + 64);
			polyAddPoints.addPoint(nmX - offset - 256 - 64, nmY - 256 - 64);
			polyAddPoints.addPoint(nmX + 256 + 64, nmY - 256 - 64);
		} else if (nmY == 8000 || nmY == 8128 || nmY == 9024 || nmY == 9152) {
			polyAddPoints.addPoint(nmX + 256 + 64, nmY + 256 + 64);
			polyAddPoints.addPoint(nmX - 256 - 64, nmY + 256 + 64);
			polyAddPoints.addPoint(nmX - 256 - 64, nmY - offset - 256 - 64);
			polyAddPoints.addPoint(nmX + 256 + 64, nmY - offset - 256 - 64);
		} else if (nmY == 6080 || nmY == 6208 || nmY == 7104 || nmY == 7232) {
			polyAddPoints.addPoint(nmX + 256 + 64, nmY + offset + 256 + 64);
			polyAddPoints.addPoint(nmX - 256 - 64, nmY + offset + 256 + 64);
			polyAddPoints.addPoint(nmX - 256 - 64, nmY - 256 - 64);
			polyAddPoints.addPoint(nmX + 256 + 64, nmY - 256 - 64);
		}
		areaAddPoints.add(new Area(polyAddPoints));
		GeneralPath path = new GeneralPath(areaAddPoints);
		renderPath(graphics, path);
	}

	private void renderPath(Graphics2D graphics, GeneralPath path) {
		graphics.setPaintMode();
		graphics.setColor(this.config.nightmareChargeBorderCol());
		graphics.setStroke(new BasicStroke(1.0F));
		path = Geometry.filterPath(path, (p1, p2) ->
				(Perspective.localToCanvas(this.client, new LocalPoint((int)p1[0], (int)p1[1]), this.client.getPlane()) != null && Perspective.localToCanvas(this.client, new LocalPoint((int)p2[0], (int)p2[1]), this.client.getPlane()) != null));
		path = Geometry.transformPath(path, coords -> {
			Point point = Perspective.localToCanvas(this.client, new LocalPoint((int)coords[0], (int)coords[1]), this.client.getPlane());
			if (point != null) {
				coords[0] = point.getX();
				coords[1] = point.getY();
			}
		});
		graphics.draw(path);
		graphics.setColor(this.config.nightmareChargeCol());
		graphics.fill(path);
	}

	private void renderHuskHighlights(Graphics2D graphics) {
		this.client.getNpcs().forEach(npc -> {
			Color color;
			int id = npc.getId();
			switch (id) {
				case 9454:
				case 9466:
					color = Color.CYAN;
					break;
				case 9455:
				case 9467:
					color = Color.GREEN;
					break;
				default:
					return;
			}
			if (!npc.isDead())
				OverlayUtil.renderPolygon(graphics, npc.getConvexHull(), color);
		});
	}

	private void drawHuskTarget(Graphics2D graphics, Map<Polygon, Player> huskTarget) {
		if (huskTarget.size() < 1)
			return;
		for (Map.Entry<Polygon, Player> entry : huskTarget.entrySet()) {
			Polygon playerPolygon = entry.getKey();
			OverlayUtil.renderPolygon(graphics, playerPolygon, this.config.huskBorderCol());
		}
	}
}
