//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.prayer;

import com.example.WidgetInfoExtended;
import com.example.theatre.TheatreConfig;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;
import java.util.Map;
import java.util.Queue;

public abstract class TheatrePrayerOverlay extends Overlay {
    private static final int TICK_PIXEL_SIZE = 60;
    private static final int BOX_WIDTH = 10;
    private static final int BOX_HEIGHT = 5;
    private final TheatreConfig config;
    private final Client client;

    @Inject
    protected TheatrePrayerOverlay(Client client, TheatreConfig config) {
        this.client = client;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.setPriority(OverlayPriority.HIGHEST);
    }

    protected abstract Queue<TheatreUpcomingAttack> getAttackQueue();

    protected abstract long getLastTick();

    protected abstract boolean isEnabled();

    public Dimension render(Graphics2D graphics) {
        Widget meleePrayerWidget = this.client.getWidget(WidgetInfoExtended.PRAYER_PROTECT_FROM_MELEE.getPackedId());
        Widget rangePrayerWidget = this.client.getWidget(WidgetInfoExtended.PRAYER_PROTECT_FROM_MISSILES.getPackedId());
        Widget magicPrayerWidget = this.client.getWidget(WidgetInfoExtended.PRAYER_PROTECT_FROM_MAGIC.getPackedId());
        boolean prayerWidgetHidden = meleePrayerWidget == null || rangePrayerWidget == null || magicPrayerWidget == null || meleePrayerWidget.isHidden() || rangePrayerWidget.isHidden() || magicPrayerWidget.isHidden();
        if (this.config.prayerHelper() && this.isEnabled() && (!prayerWidgetHidden || this.config.alwaysShowPrayerHelper())) {
            this.renderPrayerIconOverlay(graphics);
            if (this.config.descendingBoxes()) {
                this.renderDescendingBoxes(graphics);
            }
        }

        return null;
    }

    private void renderDescendingBoxes(Graphics2D graphics) {
        Map<Integer, TheatreUpcomingAttack> tickPriorityMap = TheatrePrayerUtil.getTickPriorityMap(this.getAttackQueue());
        this.getAttackQueue().forEach((attack) -> {
            int tick = attack.getTicksUntil();
            Color color = tick == 1 ? this.config.prayerColorDanger() : this.config.prayerColor();
            Widget prayerWidget = this.client.getWidget(attack.getPrayer().getWidgetInfoExtended().getPackedId());
            if (prayerWidget != null) {
                int baseX = (int)prayerWidget.getBounds().getX();
                baseX = (int)((double)baseX + prayerWidget.getBounds().getWidth() / 2.0);
                baseX -= 5;
                int baseY = (int)prayerWidget.getBounds().getY() - tick * 60 - 5;
                baseY = (int)((double)baseY + (60.0 - (double)(this.getLastTick() + 600L - System.currentTimeMillis()) / 600.0 * 60.0));
                Rectangle boxRectangle = new Rectangle(10, 5);
                boxRectangle.translate(baseX, baseY);
                if (attack.getPrayer().equals(((TheatreUpcomingAttack)tickPriorityMap.get(attack.getTicksUntil())).getPrayer())) {
                    OverlayUtil.renderPolygon(graphics, boxRectangle, color, color, new BasicStroke(2.0F));
                } else if (this.config.indicateNonPriorityDescendingBoxes()) {
                    OverlayUtil.renderPolygon(graphics, boxRectangle, color, new Color(0, 0, 0, 0), new BasicStroke(2.0F));
                }

            }
        });
    }

    private void renderPrayerIconOverlay(Graphics2D graphics) {
        TheatreUpcomingAttack attack = (TheatreUpcomingAttack)this.getAttackQueue().peek();
        if (attack != null) {
            if (!this.client.isPrayerActive(Prayer.valueOf(attack.getPrayer().name()))) {
                Widget prayerWidget = this.client.getWidget(attack.getPrayer().getWidgetInfoExtended().getPackedId());
                if (prayerWidget == null) {
                    return;
                }

                Rectangle prayerRectangle = new Rectangle((int)prayerWidget.getBounds().getWidth(), (int)prayerWidget.getBounds().getHeight());
                prayerRectangle.translate((int)prayerWidget.getBounds().getX(), (int)prayerWidget.getBounds().getY());
                OverlayUtil.renderPolygon(graphics, prayerRectangle, this.config.prayerColorDanger());
            }

        }
    }

    protected TheatreConfig getConfig() {
        return this.config;
    }
}
