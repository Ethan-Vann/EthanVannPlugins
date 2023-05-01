//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Nylocas;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.components.InfoBoxComponent;

import java.awt.*;

public class NyloSelectionBox extends Overlay {
    private final InfoBoxComponent component;
    private boolean isSelected = false;
    private boolean isHovered = false;

    public NyloSelectionBox(InfoBoxComponent component) {
        this.component = component;
    }

    public Dimension render(Graphics2D graphics) {
        if (this.isSelected) {
            this.component.setColor(Color.GREEN);
            this.component.setText("On");
        } else {
            this.component.setColor(Color.RED);
            this.component.setText("Off");
        }

        Dimension result = this.component.render(graphics);
        if (this.isHovered) {
            Color color = graphics.getColor();
            graphics.setColor(new Color(200, 200, 200));
            graphics.drawRect(this.component.getBounds().x, this.component.getBounds().y, this.component.getBounds().width, this.component.getBounds().height);
            graphics.setColor(color);
        }

        return result;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isHovered() {
        return this.isHovered;
    }

    public void setHovered(boolean isHovered) {
        this.isHovered = isHovered;
    }
}
