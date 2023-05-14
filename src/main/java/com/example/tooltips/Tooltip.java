package com.example.tooltips;

import java.awt.*;

public class Tooltip extends net.runelite.client.ui.overlay.tooltip.Tooltip {
    public Tooltip(String text) {
        super(text);
    }

    public Tooltip(String text, Color color) {
        super(text);
        this.color = color;
    }

    public Color color = null;
}
