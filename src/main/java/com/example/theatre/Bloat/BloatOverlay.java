//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Bloat;

import com.example.theatre.RoomOverlay;
import com.example.theatre.TheatreConfig;
import com.example.theatre.TheatreConfig.BLOATTIMEDOWN;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayLayer;

import javax.inject.Inject;
import java.awt.*;
import java.util.Iterator;

public class BloatOverlay extends RoomOverlay {
    @Inject
    private Bloat bloat;

    @Inject
    protected BloatOverlay(TheatreConfig config) {
        super(config);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.bloat.isBloatActive()) {
            if (this.config.bloatIndicator()) {
                this.renderPoly(graphics, this.bloat.getBloatStateColor(), this.bloat.getBloatTilePoly(), 2);
            }

            if (this.config.bloatTickCounter()) {
                NPC boss = this.bloat.getBloatNPC();
                int tick = this.bloat.getBloatTickCount();
                String ticksCounted = String.valueOf(tick);
                Point canvasPoint = boss.getCanvasTextLocation(graphics, ticksCounted, 50);
                if (this.bloat.getBloatState() > 1 && this.bloat.getBloatState() < 4 && this.config.BloatTickCountStyle() == BLOATTIMEDOWN.COUNTDOWN) {
                    this.renderTextLocation(graphics, String.valueOf(33 - this.bloat.getBloatDownCount()), Color.WHITE, canvasPoint);
                } else {
                    this.renderTextLocation(graphics, ticksCounted, Color.WHITE, canvasPoint);
                }
            }

            if (this.config.bloatHands()) {
                Iterator var6 = this.bloat.getBloatHands().keySet().iterator();

                while(var6.hasNext()) {
                    WorldPoint point = (WorldPoint)var6.next();
                    this.drawTile(graphics, point, this.config.bloatHandsColor(), this.config.bloatHandsWidth(), 255, 10);
                }
            }
        }

        return null;
    }
}
