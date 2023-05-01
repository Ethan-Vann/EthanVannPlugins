//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre;

import com.google.inject.Provides;
import com.example.theatre.Nylocas.Nylocas;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.MouseAdapter;

import javax.inject.Inject;
import java.awt.event.MouseEvent;

public class TheatreInputListener extends MouseAdapter {
    @Inject
    private Nylocas nylocas;
    @Inject
    private TheatreConfig config;

    public TheatreInputListener() {
    }

    @Provides
    TheatreConfig getConfig(ConfigManager configManager) {
        return (TheatreConfig)configManager.getConfig(TheatreConfig.class);
    }

    public MouseEvent mouseReleased(MouseEvent event) {
        if (this.nylocas.getNyloSelectionManager().isHidden()) {
            return event;
        } else if (this.nylocas.getNyloSelectionManager().getBounds().contains(event.getPoint())) {
            event.consume();
            return event;
        } else {
            return event;
        }
    }

    public MouseEvent mousePressed(MouseEvent event) {
        if (this.nylocas.getNyloSelectionManager().isHidden()) {
            return event;
        } else if (this.nylocas.getNyloSelectionManager().getBounds().contains(event.getPoint())) {
            event.consume();
            return event;
        } else {
            return event;
        }
    }

    public MouseEvent mouseClicked(MouseEvent event) {
        if (this.nylocas.getNyloSelectionManager().isHidden()) {
            return event;
        } else {
            if (event.getButton() == 1 && this.nylocas.getNyloSelectionManager().getBounds().contains(event.getPoint())) {
                if (this.nylocas.getNyloSelectionManager().getMeleeBounds().contains(event.getPoint())) {
                    this.config.setHighlightMeleeNylo(!this.config.getHighlightMeleeNylo());
                    this.nylocas.getNyloSelectionManager().getMelee().setSelected(this.config.getHighlightMeleeNylo());
                } else if (this.nylocas.getNyloSelectionManager().getRangeBounds().contains(event.getPoint())) {
                    this.config.setHighlightRangeNylo(!this.config.getHighlightRangeNylo());
                    this.nylocas.getNyloSelectionManager().getRange().setSelected(this.config.getHighlightRangeNylo());
                } else if (this.nylocas.getNyloSelectionManager().getMageBounds().contains(event.getPoint())) {
                    this.config.setHighlightMageNylo(!this.config.getHighlightMageNylo());
                    this.nylocas.getNyloSelectionManager().getMage().setSelected(this.config.getHighlightMageNylo());
                }

                event.consume();
            }

            return event;
        }
    }

    public MouseEvent mouseMoved(MouseEvent event) {
        if (this.nylocas.getNyloSelectionManager().isHidden()) {
            return event;
        } else {
            this.nylocas.getNyloSelectionManager().getMelee().setHovered(false);
            this.nylocas.getNyloSelectionManager().getRange().setHovered(false);
            this.nylocas.getNyloSelectionManager().getMage().setHovered(false);
            if (this.nylocas.getNyloSelectionManager().getBounds().contains(event.getPoint())) {
                if (this.nylocas.getNyloSelectionManager().getMeleeBounds().contains(event.getPoint())) {
                    this.nylocas.getNyloSelectionManager().getMelee().setHovered(true);
                } else if (this.nylocas.getNyloSelectionManager().getRangeBounds().contains(event.getPoint())) {
                    this.nylocas.getNyloSelectionManager().getRange().setHovered(true);
                } else if (this.nylocas.getNyloSelectionManager().getMageBounds().contains(event.getPoint())) {
                    this.nylocas.getNyloSelectionManager().getMage().setHovered(true);
                }
            }

            return event;
        }
    }
}
