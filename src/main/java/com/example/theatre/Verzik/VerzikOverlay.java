//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Verzik;

import com.example.theatre.RoomOverlay;
import com.example.theatre.TheatreConfig;
import com.example.theatre.TheatreConfig.VERZIKBALLTILE;
import com.example.theatre.Verzik.Verzik.Phase;
import com.example.theatre.Verzik.Verzik.SpecialAttack;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayUtil;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Iterator;

public class VerzikOverlay extends RoomOverlay {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");
    private static final int VERZIK_GREEN_BALL = 1598;
    private static final int VERZIK_LIGHTNING_BALL = 1585;
    @Inject
    private Verzik verzik;
    @Inject
    private SpriteManager spriteManager;

    @Inject
    protected VerzikOverlay(TheatreConfig config, SpriteManager spriteManager) {
        super(config);
        this.spriteManager = spriteManager;
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.verzik.isVerzikActive()) {
            if (this.config.verzikTileOverlay()) {
                int size = 1;
                NPCComposition composition = this.verzik.getVerzikNPC().getTransformedComposition();
                if (composition != null) {
                    size = composition.getSize();
                }

                LocalPoint lp = LocalPoint.fromWorld(this.client, this.verzik.getVerzikNPC().getWorldLocation());
                if (lp != null) {
                    Polygon tilePoly = getCanvasTileAreaPoly(this.client, lp, size, false);
                    if (tilePoly != null) {
                        if (this.verzik.isVerzikEnraged()) {
                            this.renderPoly(graphics, new Color(255, 110, 90), tilePoly);
                        } else {
                            this.renderPoly(graphics, new Color(255, 110, 230), tilePoly);
                        }
                    }
                }
            }

            String tick_text = "";
            if (this.config.verzikAttackCounter() && this.verzik.getVerzikSpecial() != SpecialAttack.WEBS) {
                tick_text = tick_text + "[A] " + this.verzik.getVerzikAttackCount();
                if (this.config.verzikAutosTick() || this.config.verzikTotalTickCounter()) {
                    tick_text = tick_text + " : ";
                }
            }

            if (this.config.verzikAutosTick() && this.verzik.getVerzikSpecial() != SpecialAttack.WEBS) {
                tick_text = tick_text + this.verzik.getVerzikTicksUntilAttack();
                if (this.config.verzikTotalTickCounter()) {
                    tick_text = tick_text + " : ";
                }
            }

            if (this.config.verzikTotalTickCounter()) {
                tick_text = tick_text + "(" + this.verzik.getVerzikTotalTicksUntilAttack() + ")";
            }

            Point canvasPoint = this.verzik.getVerzikNPC().getCanvasTextLocation(graphics, tick_text, 60);
            if (canvasPoint != null) {
                Color col = this.verzik.verzikSpecialWarningColor();
                this.renderTextLocation(graphics, tick_text, col, canvasPoint);
            }

            Iterator iterator;
            if (this.verzik.getVerzikPhase() == Phase.PHASE2) {
                if (this.config.verzikProjectiles()) {
                    iterator = this.verzik.getVerzikRangeProjectiles().values().iterator();

                    while(iterator.hasNext()) {
                        this.drawTile(graphics, (WorldPoint)iterator.next(), this.config.verzikProjectilesColor(), 1, 255, 20);
                    }
                }

                if (this.config.verzikReds()) {
                    this.verzik.getVerzikReds().forEach((k, v) -> {
                        int v_health = (Integer)v.getValue();
                        int v_healthRation = (Integer)v.getKey();
                        if (k.getName() != null && k.getHealthScale() > 0) {
                            v_health = k.getHealthScale();
                            v_healthRation = Math.min(v_healthRation, k.getHealthRatio());
                        }

                        float percentage = (float)v_healthRation / (float)v_health * 100.0F;
                        Point textLocation = k.getCanvasTextLocation(graphics, String.valueOf(DECIMAL_FORMAT.format((double)percentage)), 80);
                        if (textLocation != null) {
                            this.renderTextLocation(graphics, String.valueOf(DECIMAL_FORMAT.format((double)percentage)), Color.WHITE, textLocation);
                        }

                    });
                    NPC[] reds = (NPC[])this.verzik.getVerzikReds().keySet().toArray(new NPC[0]);
                    NPC[] var15 = reds;
                    int var6 = reds.length;

                    for(int var7 = 0; var7 < var6; ++var7) {
                        NPC npc = var15[var7];
                        if (npc.getName() != null && npc.getHealthScale() > 0 && npc.getHealthRatio() < 100) {
                            Pair<Integer, Integer> newVal = new MutablePair(npc.getHealthRatio(), npc.getHealthScale());
                            if (this.verzik.getVerzikReds().containsKey(npc)) {
                                this.verzik.getVerzikReds().put(npc, newVal);
                            }
                        }
                    }
                }

                if (this.verzik.getVerzikPhase() == Phase.PHASE2 && this.verzik.getVerzikNPC() != null && this.config.lightningAttackHelper()) {
                    Point imageLocation;
                    if (this.verzik.getVerzikLightningAttacks() == 0) {
                        BufferedImage lightningIcon = this.spriteManager.getSprite(558, 0);
                        imageLocation = this.verzik.getVerzikNPC().getCanvasImageLocation(lightningIcon, 200);
                        if (imageLocation != null) {
                            OverlayUtil.renderImageLocation(graphics, imageLocation, lightningIcon);
                        }
                    } else {
                        String attacksLeft = Integer.toString(this.verzik.getVerzikLightningAttacks());
                        imageLocation = Perspective.getCanvasTextLocation(this.client, graphics, this.verzik.getVerzikNPC().getLocalLocation(), attacksLeft, 200);
                        this.renderTextLocation(graphics, attacksLeft, Color.WHITE, imageLocation);
                    }
                }

                if (this.config.lightningAttackTick()) {
                    this.client.getProjectiles().forEach((px) -> {
                        Actor getInteracting = px.getInteracting();
                        if (px.getId() == 1585) {
                            Player localPlayer = this.client.getLocalPlayer();
                            if (getInteracting != null && getInteracting == localPlayer) {
                                Point point = this.getProjectilePoint(px);
                                if (point != null) {
                                    Point textLocation = new Point(point.getX(), point.getY());
                                    this.renderTextLocation(graphics, Integer.toString(px.getRemainingCycles() / 30), Color.ORANGE, textLocation);
                                }
                            }
                        }

                    });
                }

                if (this.verzik.isHM() && this.config.verzikPoisonTileHighlight()) {
                    this.verzik.getVerzikPoisonTiles().stream().filter(VerzikPoisonTile::shouldHighlight).forEach((tile) -> {
                        this.drawTile(graphics, tile.getTile(), this.config.verzikPoisonTileHighlightColor(), 1, 255, 20);
                    });
                }
            }

            if (this.verzik.getVerzikPhase() == Phase.PHASE3) {
                if (this.config.verzikDisplayTank() && this.verzik.getVerzikNPC().getInteracting() != null) {
                    Polygon tilePoly = this.verzik.getVerzikNPC().getInteracting().getCanvasTilePoly();
                    if (tilePoly != null) {
                        this.renderPoly(graphics, Color.LIGHT_GRAY, tilePoly);
                    }
                }

                if (this.config.verzikTornado() && (!this.config.verzikPersonalTornadoOnly() || this.config.verzikPersonalTornadoOnly() && this.verzik.getVerzikLocalTornado() != null)) {
                    this.verzik.getVerzikTornadoes().forEach((k) -> {
                        if (k.getCurrentPosition() != null) {
                            this.drawTile(graphics, k.getCurrentPosition(), this.config.verzikTornadoColor(), 1, 120, 10);
                        }

                        if (k.getLastPosition() != null) {
                            this.drawTile(graphics, k.getLastPosition(), this.config.verzikTornadoColor(), 2, 180, 20);
                        }

                    });
                }

                if (this.config.verzikYellows() && this.verzik.getVerzikYellows() > 0) {
                    String text = Integer.toString(this.verzik.getVerzikYellows());
                    Iterator var21 = this.client.getGraphicsObjects().iterator();

                    while(var21.hasNext()) {
                        GraphicsObject object = (GraphicsObject)var21.next();
                        if (object.getId() == 1595) {
                            this.drawTile(graphics, WorldPoint.fromLocal(this.client, object.getLocation()), Color.YELLOW, 1, 255, 0);
                            LocalPoint lp = object.getLocation();
                            Point point = Perspective.getCanvasTextLocation(this.client, graphics, lp, text, 0);
                            this.renderTextLocation(graphics, text, Color.WHITE, point);
                        }
                    }
                }

                if (this.config.verzikGreenBall() || this.config.verzikGreenBallTick()) {
                    iterator = this.client.getProjectiles().iterator();

                    while(iterator.hasNext()) {
                        Projectile p = (Projectile)iterator.next();
                        if (p.getId() == 1598) {
                            if (this.config.verzikGreenBallTick()) {
                                Point point = this.getProjectilePoint(p);
                                if (point != null) {
                                    Point textLocation = new Point(point.getX(), point.getY());
                                    this.renderTextLocation(graphics, Integer.toString(p.getRemainingCycles() / 30), Color.GREEN, textLocation);
                                }
                            }

                            if (this.config.verzikGreenBall()) {
                                Polygon tilePoly;
                                if (this.config.verzikGreenBallMarker() == VERZIKBALLTILE.TILE) {
                                    tilePoly = p.getInteracting().getCanvasTilePoly();
                                } else {
                                    tilePoly = getCanvasTileAreaPoly(this.client, p.getInteracting().getLocalLocation(), 3, true);
                                }

                                if (tilePoly != null) {
                                    this.renderPoly(graphics, this.config.verzikGreenBallColor(), tilePoly);
                                }
                            }
                        }
                    }
                }
            }

            if ((this.verzik.getVerzikPhase() == Phase.PHASE2 || this.verzik.getVerzikPhase() == Phase.PHASE3) && (this.config.verzikNyloPersonalWarning() || this.config.verzikNyloOtherWarning())) {
                this.verzik.getVerzikAggros().forEach((k) -> {
                    if (k.getInteracting() != null && !k.isDead() && (this.config.verzikNyloPersonalWarning() && k.getInteracting() == this.client.getLocalPlayer() || this.config.verzikNyloOtherWarning() && k.getInteracting() != this.client.getLocalPlayer())) {
                        Color color = Color.LIGHT_GRAY;
                        if (k.getInteracting() == this.client.getLocalPlayer()) {
                            color = Color.YELLOW;
                        }

                        Point textLocation = k.getCanvasTextLocation(graphics, k.getInteracting().getName(), 80);
                        if (textLocation != null) {
                            OverlayUtil.renderTextLocation(graphics, textLocation, k.getInteracting().getName(), color);
                        }

                        if (this.config.verzikNyloExplodeAOE()) {
                            int size = 1;
                            int thick_size = 1;
                            NPCComposition composition = k.getComposition();
                            if (composition != null) {
                                size = composition.getSize() + 2 * thick_size;
                            }

                            LocalPoint lp = LocalPoint.fromWorld(this.client, k.getWorldLocation());
                            if (lp != null) {
                                lp = new LocalPoint(lp.getX() - thick_size * 128, lp.getY() - thick_size * 128);
                                Polygon tilePoly = getCanvasTileAreaPoly(this.client, lp, size, false);
                                if (tilePoly != null) {
                                    this.renderPoly(graphics, color, tilePoly);
                                }
                            }
                        }
                    }

                });
            }
        }

        return null;
    }

    private Point getProjectilePoint(Projectile p) {
        int x = (int)p.getX();
        int y = (int)p.getY();
        int z = (int)p.getZ();
        return Perspective.localToCanvas(this.client, new LocalPoint(x, y), 0, Perspective.getTileHeight(this.client, new LocalPoint(x, y), p.getFloor()) - z);
    }
}
