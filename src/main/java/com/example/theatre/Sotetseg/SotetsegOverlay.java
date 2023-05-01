//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Sotetseg;

import com.example.theatre.RoomOverlay;
import com.example.theatre.TheatreConfig;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;
import java.util.Iterator;

public class SotetsegOverlay extends RoomOverlay {
    @Inject
    private Sotetseg sotetseg;

    @Inject
    protected SotetsegOverlay(TheatreConfig config) {
        super(config);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPriority(OverlayPriority.MED);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.sotetseg.isSotetsegActive()) {
            int counter;
            NPC boss;
            String attacksCounted;
            Point point;
            if (this.config.sotetsegAutoAttacksTicks()) {
                counter = this.sotetseg.getSotetsegTickCount();
                if (counter >= 0) {
                    boss = this.sotetseg.getSotetsegNPC();
                    attacksCounted = String.valueOf(counter);
                    point = boss.getCanvasTextLocation(graphics, attacksCounted, 50);
                    this.renderTextLocation(graphics, attacksCounted, Color.WHITE, point);
                }
            }

            if (this.config.sotetsegAttackCounter()) {
                counter = this.sotetseg.getAttacksLeft();
                if (counter >= 0) {
                    boss = this.sotetseg.getSotetsegNPC();
                    attacksCounted = String.valueOf(this.sotetseg.getAttacksLeft());
                    point = boss.getCanvasTextLocation(graphics, attacksCounted, 250);
                    this.renderTextLocation(graphics, attacksCounted, Color.YELLOW, point);
                }
            }

            if (this.config.sotetsegOrbAttacksTicks() || this.config.sotetsegBigOrbTicks()) {
                Iterator var8 = this.client.getProjectiles().iterator();

                label87:
                while(true) {
                    Projectile p;
                    int id;
                    do {
                        if (!var8.hasNext()) {
                            break label87;
                        }

                        p = (Projectile)var8.next();
                        id = p.getId();
                        point = Perspective.localToCanvas(this.client, new LocalPoint((int)p.getX(), (int)p.getY()), 0, Perspective.getTileHeight(this.client, new LocalPoint((int)p.getX(), (int)p.getY()), p.getFloor()) - (int)p.getZ());
                    } while(point == null);

                    if (p.getInteracting() == this.client.getLocalPlayer() && (id == 1606 || id == 1607) && this.config.sotetsegOrbAttacksTicks()) {
                        this.renderTextLocation(graphics, (id == 1606 ? "M" : "R") + p.getRemainingCycles() / 30, id == 1606 ? Color.CYAN : Color.GREEN, point);
                    }

                    if (id == 1604 && this.config.sotetsegBigOrbTicks()) {
                        this.renderTextLocation(graphics, String.valueOf(p.getRemainingCycles() / 30), this.config.sotetsegBigOrbTickColor(), point);
                        this.renderPoly(graphics, this.config.sotetsegBigOrbTileColor(), p.getInteracting().getCanvasTilePoly());
                    }
                }
            }

            if (this.config.sotetsegMaze()) {
                counter = 1;

                Iterator var10;
                Point p;
                WorldPoint wp;
                for(var10 = this.sotetseg.getRedTiles().iterator(); var10.hasNext(); ++counter) {
                    p = (Point)var10.next();
                    wp = this.sotetseg.worldPointFromMazePoint(p);
                    this.drawTile(graphics, wp, Color.WHITE, 1, 255, 0);
                    LocalPoint lp = LocalPoint.fromWorld(this.client, wp);
                    if (lp != null && !this.sotetseg.isWasInUnderWorld()) {
                        Point textPoint = Perspective.getCanvasTextLocation(this.client, graphics, lp, String.valueOf(counter), 0);
                        if (textPoint != null) {
                            this.renderTextLocation(graphics, String.valueOf(counter), Color.WHITE, textPoint);
                        }
                    }
                }

                var10 = this.sotetseg.getGreenTiles().iterator();

                while(var10.hasNext()) {
                    p = (Point)var10.next();
                    wp = this.sotetseg.worldPointFromMazePoint(p);
                    this.drawTile(graphics, wp, Color.GREEN, 1, 255, 0);
                }
            }
        }

        return null;
    }
}
