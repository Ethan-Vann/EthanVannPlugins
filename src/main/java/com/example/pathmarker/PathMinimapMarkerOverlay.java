package com.example.pathmarker;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Varbits;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;

public class PathMinimapMarkerOverlay extends Overlay
{
    private final Client client;
    private final PathMarkerPlugin plugin;

    @Inject
    private PathMarkerConfig config;

    private double angle;

    @Inject
    private PathMinimapMarkerOverlay(Client client, PathMarkerConfig config, PathMarkerPlugin plugin)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        angle = client.getMapAngle() * 0.0030679615D;
        Widget minimapDrawWidget;
        if (client.isResized())
        {
            if (client.getVarbitValue(Varbits.SIDE_PANELS) == 1)
            {
                minimapDrawWidget = client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_DRAW_AREA);
            }
            else
            {
                minimapDrawWidget = client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_STONES_DRAW_AREA);
            }
        }
        else
        {
            minimapDrawWidget = client.getWidget(WidgetInfo.FIXED_VIEWPORT_MINIMAP_DRAW_AREA);
        }
        if (minimapDrawWidget == null || minimapDrawWidget.isHidden())
        {
            return null;
        }
        Point minimapWidgetLocation = minimapDrawWidget.getCanvasLocation();
        Point minimapPoint = new Point( minimapWidgetLocation.getX() + minimapDrawWidget.getWidth()/2, minimapWidgetLocation.getY() + minimapDrawWidget.getHeight()/2);
        graphics.rotate(angle, minimapPoint.getX(), minimapPoint.getY());
        if ((config.hoverPathDisplaySetting() != PathMarkerConfig.PathDisplaySetting.NEVER)
                && (config.activePathDisplaySetting() == PathMarkerConfig.PathDisplaySetting.NEVER || !plugin.isPathActive() || !config.drawOnlyIfNoActivePath())
                && (plugin.isKeyDisplayHoverPath() || config.hoverPathDisplaySetting() == PathMarkerConfig.PathDisplaySetting.ALWAYS))
        {
            for (WorldPoint worldPoint : plugin.getHoverPathTiles())
            {
                if (config.hoverPathDrawLocations() == PathMarkerConfig.drawLocations.BOTH || config.hoverPathDrawLocations() == PathMarkerConfig.drawLocations.MINIMAP)
                {
                    if (config.hoverPathDrawMode() == PathMarkerConfig.DrawMode.FULL_PATH || worldPoint == plugin.getHoverPathTiles().get(plugin.getHoverPathTiles().size() - 1))
                    {
                        renderMinimapTile(graphics, worldPoint, config.hoverPathFill1(), minimapPoint);
                    }
                }
            }
            for (WorldPoint worldPoint : plugin.getHoverMiddlePathTiles())
            {
                if (config.hoverPathDrawLocations() == PathMarkerConfig.drawLocations.BOTH || config.hoverPathDrawLocations() == PathMarkerConfig.drawLocations.MINIMAP)
                {
                    if (config.hoverPathDrawMode() == PathMarkerConfig.DrawMode.FULL_PATH || worldPoint == plugin.getHoverPathTiles().get(plugin.getHoverPathTiles().size() - 1))
                    {
                        renderMinimapTile(graphics, worldPoint, config.hoverPathFill2(), minimapPoint);
                    }
                }
            }
        }
        if ((config.activePathDisplaySetting() != PathMarkerConfig.PathDisplaySetting.NEVER) && plugin.isPathActive()
                && (plugin.isKeyDisplayActivePath() || config.activePathDisplaySetting() == PathMarkerConfig.PathDisplaySetting.ALWAYS))
        {
            for (WorldPoint worldPoint : plugin.getActivePathTiles())
            {
                if (config.activePathDrawLocations() == PathMarkerConfig.drawLocations.BOTH || config.activePathDrawLocations() == PathMarkerConfig.drawLocations.MINIMAP)
                {
                    if (config.activePathDrawMode() == PathMarkerConfig.DrawMode.FULL_PATH || worldPoint == plugin.getActivePathTiles().get(plugin.getActivePathTiles().size() - 1))
                    {
                        renderMinimapTile(graphics, worldPoint, config.activePathFill1(), minimapPoint);
                    }
                }
            }
            for (WorldPoint worldPoint : plugin.getActiveMiddlePathTiles())
            {
                if (config.activePathDrawLocations() == PathMarkerConfig.drawLocations.BOTH || config.activePathDrawLocations() == PathMarkerConfig.drawLocations.MINIMAP)
                {
                    if (config.activePathDrawMode() == PathMarkerConfig.DrawMode.FULL_PATH || worldPoint == plugin.getActivePathTiles().get(plugin.getActivePathTiles().size() - 1))
                    {
                        renderMinimapTile(graphics, worldPoint, config.activePathFill2(), minimapPoint);
                    }
                }
            }
        }
        graphics.rotate(-angle, minimapPoint.getX(), minimapPoint.getY());
        return null;
    }

    private void renderMinimapTile(Graphics2D graphics, WorldPoint worldPoint, Color color, Point miniMapPoint)
    {
        LocalPoint lp = LocalPoint.fromWorld(client, worldPoint);
        if (lp == null)
        {
            return;
        }
        LocalPoint localLocation = client.getLocalPlayer().getLocalLocation();
        if (localLocation == null)
        {
            return;
        }
        int x = lp.getX()/32 - localLocation.getX()/32;
        int y = localLocation.getY()/32 - lp.getY()/32;
        int squaredDistance = x*x + y*y;
        if (squaredDistance > 5900)
        {
            return;
        }
        Point miniMapPoint2 = new Point(x + miniMapPoint.getX(), y + miniMapPoint.getY());
        graphics.setColor(color);
        graphics.fillRect(miniMapPoint2.getX() - 2, miniMapPoint2.getY() - 2, 4, 4);
    }
}
