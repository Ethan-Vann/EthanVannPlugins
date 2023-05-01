//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Nylocas;

import com.example.theatre.TheatreConfig;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class NylocasAliveCounterOverlay extends Overlay {
    private TheatreConfig config;
    private LineComponent waveComponent;
    private final PanelComponent panelComponent = new PanelComponent();
    private static final String prefix = "Nylocas Alive: ";
    private Instant nyloWaveStart;
    private int nyloAlive = 0;
    private int maxNyloAlive = 12;
    private int wave = 0;
    private boolean hidden = false;

    @Inject
    private NylocasAliveCounterOverlay(TheatreConfig config) {
        this.config = config;
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.refreshPanel();
    }

    public void setNyloAlive(int aliveCount) {
        this.nyloAlive = aliveCount;
        this.refreshPanel();
    }

    public void setMaxNyloAlive(int maxAliveCount) {
        this.maxNyloAlive = maxAliveCount;
        this.refreshPanel();
    }

    public void setWave(int wave) {
        this.wave = wave;
        this.refreshPanel();
    }

    private void refreshPanel() {
        LineComponent lineComponent = LineComponent.builder().left("Nylocas Alive: ").right(this.nyloAlive + "/" + this.maxNyloAlive).build();
        if (this.nyloAlive >= this.maxNyloAlive) {
            lineComponent.setRightColor(Color.ORANGE);
        } else {
            lineComponent.setRightColor(Color.GREEN);
        }

        this.waveComponent = LineComponent.builder().left("Wave: " + this.wave + "/" + 31).build();
        this.panelComponent.getChildren().clear();
        this.panelComponent.getChildren().add(this.waveComponent);
        this.panelComponent.getChildren().add(lineComponent);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.config.nyloAlivePanel() && !this.isHidden()) {
            if (this.nyloWaveStart != null) {
                Iterator var2 = this.panelComponent.getChildren().iterator();

                while(var2.hasNext()) {
                    LayoutableRenderableEntity entity = (LayoutableRenderableEntity)var2.next();
                    if (entity instanceof LineComponent && entity.equals(this.waveComponent)) {
                        ((LineComponent)entity).setRight(this.getFormattedTime());
                    }
                }
            }

            return this.panelComponent.render(graphics);
        } else {
            return null;
        }
    }

    public String getFormattedTime() {
        Duration duration = Duration.between(this.nyloWaveStart, Instant.now());
        LocalTime localTime = LocalTime.ofSecondOfDay(duration.getSeconds());
        return localTime.format(DateTimeFormatter.ofPattern("mm:ss"));
    }

    public void setNyloWaveStart(Instant nyloWaveStart) {
        this.nyloWaveStart = nyloWaveStart;
    }

    public int getNyloAlive() {
        return this.nyloAlive;
    }

    public int getMaxNyloAlive() {
        return this.maxNyloAlive;
    }

    public int getWave() {
        return this.wave;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
