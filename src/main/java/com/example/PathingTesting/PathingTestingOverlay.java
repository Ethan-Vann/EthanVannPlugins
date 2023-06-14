package com.example.PathingTesting;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import java.awt.*;

public class PathingTestingOverlay extends Overlay {
    PathingTesting plugin;
    Client client;
    PathingTestingOverlay(Client client, PathingTesting plugin) {
        this.plugin = plugin;
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if(plugin.path==null){
            return null;
        }
        for (WorldPoint worldPoint : plugin.path) {
            OverlayUtil.renderPolygon(graphics, Perspective.getCanvasTilePoly(client, LocalPoint.fromWorld(client,
                    worldPoint)), Color.blue, new Color(0, 0, 0, 0), new BasicStroke((float) 1));
        }
        return null;
    }
}
