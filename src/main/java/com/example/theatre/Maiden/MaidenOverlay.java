//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Maiden;

import com.google.common.collect.ArrayListMultimap;
import com.example.theatre.RoomOverlay;
import com.example.theatre.TheatreConfig;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayLayer;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Iterator;

public class MaidenOverlay extends RoomOverlay {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");
    @Inject
    private Maiden maiden;
    @Inject
    private Client client;

    @Inject
    protected MaidenOverlay(TheatreConfig config) {
        super(config);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.maiden.isMaidenActive()) {
            Iterator var2;
            WorldPoint point;
            if (this.config.maidenBlood()) {
                var2 = this.maiden.getMaidenBloodSplatters().iterator();

                while(var2.hasNext()) {
                    point = (WorldPoint)var2.next();
                    this.drawTile(graphics, point, new Color(0, 150, 200), 2, 150, 10);
                }
            }

            if (this.config.maidenSpawns()) {
                var2 = this.maiden.getMaidenBloodSpawnLocations().iterator();

                while(var2.hasNext()) {
                    point = (WorldPoint)var2.next();
                    this.drawTile(graphics, point, new Color(0, 150, 200), 2, 180, 20);
                }

                var2 = this.maiden.getMaidenBloodSpawnTrailingLocations().iterator();

                while(var2.hasNext()) {
                    point = (WorldPoint)var2.next();
                    this.drawTile(graphics, point, new Color(0, 150, 200), 1, 120, 10);
                }
            }

            if (this.config.maidenTickCounter() && this.maiden.getMaidenNPC() != null && !this.maiden.getMaidenNPC().isDead()) {
                String text = String.valueOf(this.maiden.getTicksUntilAttack());
                Point canvasPoint = this.maiden.getMaidenNPC().getCanvasTextLocation(graphics, text, 15);
                if (canvasPoint != null) {
                    Color col = this.maiden.maidenSpecialWarningColor();
                    this.renderTextLocation(graphics, text, col, canvasPoint);
                }
            }

            if (this.maiden.isMaidenActive() && (this.config.maidenRedsHealth() || this.config.maidenRedsDistance())) {
                this.displayNyloHpOverlayGrouped(graphics);
                NPC[] reds = (NPC[])this.maiden.getMaidenReds().keySet().toArray(new NPC[0]);
                NPC[] var11 = reds;
                int var12 = reds.length;

                for(int var5 = 0; var5 < var12; ++var5) {
                    NPC npc = var11[var5];
                    if (npc.getName() != null && npc.getHealthScale() > 0 && npc.getHealthRatio() < 100) {
                        Pair<Integer, Integer> newVal = new MutablePair(npc.getHealthRatio(), npc.getHealthScale());
                        if (this.maiden.getMaidenReds().containsKey(npc)) {
                            this.maiden.getMaidenReds().put(npc, newVal);
                        }
                    }
                }
            }
        }

        return null;
    }

    private void displayNyloHpOverlayGrouped(Graphics2D graphics) {
        ArrayListMultimap<Point, NPC> nyloGrouped = ArrayListMultimap.create();
        this.maiden.getMaidenReds().forEach((nylo, hp) -> {
            Point point = new Point(nylo.getWorldLocation().getX(), nylo.getWorldLocation().getY());
            if (!nylo.isDead()) {
                nyloGrouped.put(point, nylo);
                if (nylo.getName() != null && nylo.getHealthScale() > 0 && nylo.getHealthRatio() < 100 && this.maiden.getMaidenReds().containsKey(nylo)) {
                    this.maiden.getMaidenReds().put(nylo, new MutablePair(nylo.getHealthRatio(), nylo.getHealthScale()));
                }
            }

        });
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Iterator var4 = nyloGrouped.keys().iterator();

        while(var4.hasNext()) {
            Point point = (Point)var4.next();
            int zOffset = 0;

            for(Iterator<NPC> iterator = nyloGrouped.get(point).iterator(); iterator.hasNext(); zOffset += fontMetrics.getHeight()) {
                NPC nyloNPC = (NPC)iterator.next();
                this.drawNyloHpOverlay(graphics, nyloNPC, zOffset);
            }
        }

    }

    private void drawNyloHpOverlay(Graphics2D graphics, NPC nyloNPC, int zOffset) {
        int healthScale = nyloNPC.getHealthScale();
        int healthRatio = nyloNPC.getHealthRatio();
        if (nyloNPC.getName() != null && nyloNPC.getHealthScale() > 0) {
            healthScale = nyloNPC.getHealthScale();
            healthRatio = Math.min(healthRatio, nyloNPC.getHealthRatio());
        }

        float nyloHp = (float)healthRatio / (float)healthScale * 100.0F;
        String text = this.getNyloString(nyloNPC);
        Point textLocation = nyloNPC.getCanvasTextLocation(graphics, text, 0);
        if (!nyloNPC.isDead() && textLocation != null) {
            textLocation = new Point(textLocation.getX(), textLocation.getY() - zOffset);
            Color color = this.percentageToColor(nyloHp);
            this.renderTextLocation(graphics, text, color, textLocation);
        }

    }

    private String getNyloString(NPC nyloNPC) {
        String string = "";
        int maidenX;
        int deltaX;
        if (this.config.maidenRedsHealth()) {
            maidenX = nyloNPC.getHealthScale();
            deltaX = nyloNPC.getHealthRatio();
            if (nyloNPC.getName() != null && nyloNPC.getHealthScale() > 0) {
                maidenX = nyloNPC.getHealthScale();
                deltaX = Math.min(deltaX, nyloNPC.getHealthRatio());
            }

            float percentage = (float)deltaX / (float)maidenX * 100.0F;
            string = String.valueOf(DECIMAL_FORMAT.format((double)percentage));
        }

        if (this.config.maidenRedsHealth() && this.config.maidenRedsDistance()) {
            string = string + " - ";
        }

        if (this.config.maidenRedsDistance()) {
            maidenX = this.maiden.getMaidenNPC().getWorldLocation().getX() + this.maiden.getMaidenNPC().getTransformedComposition().getSize();
            deltaX = Math.max(0, nyloNPC.getWorldLocation().getX() - maidenX);
            string = string + deltaX;
        }

        return string;
    }

    private Color percentageToColor(float percentage) {
        percentage = Math.max(Math.min(100.0F, percentage), 0.0F);
        double rMod = 130.0 * (double)percentage / 100.0;
        double gMod = 235.0 * (double)percentage / 100.0;
        double bMod = 125.0 * (double)percentage / 100.0;
        return new Color((int)Math.min(255.0, 255.0 - rMod), Math.min(255, (int)(20.0 + gMod)), Math.min(255, (int)(0.0 + bMod)));
    }
}
