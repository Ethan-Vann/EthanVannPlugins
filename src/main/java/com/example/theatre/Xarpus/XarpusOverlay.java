//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Xarpus;

import com.example.theatre.Direction;
import com.example.theatre.RoomOverlay;
import com.example.theatre.TheatreConfig;
import com.example.theatre.TheatreConfig.XARPUS_LINE_OF_SIGHT;
import com.example.theatre.TheatrePlugin;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayLayer;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class XarpusOverlay extends RoomOverlay {
    @Inject
    private Xarpus xarpus;
    private static final Function<WorldPoint, Point[]> getNEBoxPoints = (p) -> {
        return new Point[]{new Point(p.getX(), p.getY()), new Point(p.getX(), p.getY() + 8), new Point(p.getX() + 8, p.getY() + 8), new Point(p.getX() + 8, p.getY())};
    };
    private static final Function<WorldPoint, Point[]> getNWBoxPoints = (p) -> {
        return new Point[]{new Point(p.getX() - 8, p.getY()), new Point(p.getX() - 8, p.getY() + 8), new Point(p.getX(), p.getY() + 8), new Point(p.getX(), p.getY())};
    };
    private static final Function<WorldPoint, Point[]> getSEBoxPoints = (p) -> {
        return new Point[]{new Point(p.getX(), p.getY() - 8), new Point(p.getX(), p.getY()), new Point(p.getX() + 8, p.getY()), new Point(p.getX() + 8, p.getY() - 8)};
    };
    private static final Function<WorldPoint, Point[]> getSWBoxPoints = (p) -> {
        return new Point[]{new Point(p.getX() - 8, p.getY() - 8), new Point(p.getX() - 8, p.getY()), new Point(p.getX(), p.getY()), new Point(p.getX(), p.getY() - 8)};
    };
    private static final Function<WorldPoint, Point[]> getNEMeleePoints = (p) -> {
        return new Point[]{new Point(p.getX() + 4, p.getY() + 4), new Point(p.getX(), p.getY() + 4), new Point(p.getX(), p.getY() + 3), new Point(p.getX() + 3, p.getY() + 3), new Point(p.getX() + 3, p.getY()), new Point(p.getX() + 4, p.getY())};
    };
    private static final Function<WorldPoint, Point[]> getNWMeleePoints = (p) -> {
        return new Point[]{new Point(p.getX() - 4, p.getY() + 4), new Point(p.getX() - 4, p.getY()), new Point(p.getX() - 3, p.getY()), new Point(p.getX() - 3, p.getY() + 3), new Point(p.getX(), p.getY() + 3), new Point(p.getX(), p.getY() + 4)};
    };
    private static final Function<WorldPoint, Point[]> getSEMeleePoints = (p) -> {
        return new Point[]{new Point(p.getX() + 4, p.getY() - 4), new Point(p.getX() + 4, p.getY()), new Point(p.getX() + 3, p.getY()), new Point(p.getX() + 3, p.getY() - 3), new Point(p.getX(), p.getY() - 3), new Point(p.getX(), p.getY() - 4)};
    };
    private static final Function<WorldPoint, Point[]> getSWMeleePoints = (p) -> {
        return new Point[]{new Point(p.getX() - 4, p.getY() - 4), new Point(p.getX(), p.getY() - 4), new Point(p.getX(), p.getY() - 3), new Point(p.getX() - 3, p.getY() - 3), new Point(p.getX() - 3, p.getY()), new Point(p.getX() - 4, p.getY())};
    };

    @Inject
    protected XarpusOverlay(TheatreConfig config) {
        super(config);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.xarpus.isInstanceTimerRunning() && !this.xarpus.isExhumedSpawned() && this.xarpus.inRoomRegion(TheatrePlugin.XARPUS_REGION) && this.config.xarpusInstanceTimer()) {
            Player player = this.client.getLocalPlayer();
            if (player != null) {
                Point point = player.getCanvasTextLocation(graphics, "#", player.getLogicalHeight() + 60);
                if (point != null) {
                    this.renderTextLocation(graphics, String.valueOf(this.xarpus.getInstanceTimer()), Color.CYAN, point);
                }
            }
        }

        if (this.xarpus.isXarpusActive()) {
            NPC boss = this.xarpus.getXarpusNPC();
            if (this.config.xarpusTickP2() && (boss.getId() == 8340 || boss.getId() == 10768 || boss.getId() == 10772) || this.config.xarpusTickP3() && (boss.getId() == 8341 || boss.getId() == 10769 || boss.getId() == 10773)) {
                int tick = this.xarpus.getXarpusTicksUntilAttack();
                String ticksLeftStr = String.valueOf(tick);
                Point canvasPoint = boss.getCanvasTextLocation(graphics, ticksLeftStr, 130);
                this.renderTextLocation(graphics, ticksLeftStr, Color.WHITE, canvasPoint);
            }

            if ((this.config.xarpusExhumed() || this.config.xarpusExhumedTick()) && (boss.getId() == 8339 || boss.getId() == 10767 || boss.getId() == 10771) && !this.xarpus.getXarpusExhumeds().isEmpty()) {
                Collection<Pair<GroundObject, Integer>> exhumeds = this.xarpus.getXarpusExhumeds().values();
                exhumeds.forEach((p) -> {
                    GroundObject o = (GroundObject)p.getLeft();
                    int ticks = (Integer)p.getRight();
                    if (this.config.xarpusExhumed()) {
                        Polygon poly = o.getCanvasTilePoly();
                        if (poly != null) {
                            graphics.setColor(new Color(0, 255, 0, 130));
                            graphics.setStroke(new BasicStroke(1.0F));
                            graphics.draw(poly);
                        }
                    }

                    if (this.config.xarpusExhumedTick()) {
                        String count = Integer.toString(ticks);
                        LocalPoint lp = o.getLocalLocation();
                        Point point = Perspective.getCanvasTextLocation(this.client, graphics, lp, count, 0);
                        if (point != null) {
                            this.renderTextLocation(graphics, count, Color.WHITE, point);
                        }
                    }

                });
            }

            if (this.config.xarpusLineOfSight() != XARPUS_LINE_OF_SIGHT.OFF) {
                this.renderLineOfSightPolygon(graphics);
            }
        }

        return null;
    }

    private void renderLineOfSightPolygon(Graphics2D graphics) {
        NPC xarpusNpc = this.xarpus.getXarpusNPC();
        if (xarpusNpc != null && (xarpusNpc.getId() == 8340 || xarpusNpc.getId() == 10768 || xarpusNpc.getId() == 10772) && !xarpusNpc.isDead() && this.xarpus.isPostScreech()) {
            WorldPoint xarpusWorldPoint = WorldPoint.fromLocal(this.client, xarpusNpc.getLocalLocation());
            Direction dir = Direction.getPreciseDirection(xarpusNpc.getOrientation());
            if (dir != null) {
                boolean markMeleeTiles = this.config.xarpusLineOfSight() == XARPUS_LINE_OF_SIGHT.MELEE_TILES;
                Point[] points;
                switch (dir) {
                    case NORTHEAST:
                        points = markMeleeTiles ? (Point[])getNEMeleePoints.apply(xarpusWorldPoint) : (Point[])getNEBoxPoints.apply(xarpusWorldPoint);
                        break;
                    case NORTHWEST:
                        points = markMeleeTiles ? (Point[])getNWMeleePoints.apply(xarpusWorldPoint) : (Point[])getNWBoxPoints.apply(xarpusWorldPoint);
                        break;
                    case SOUTHEAST:
                        points = markMeleeTiles ? (Point[])getSEMeleePoints.apply(xarpusWorldPoint) : (Point[])getSEBoxPoints.apply(xarpusWorldPoint);
                        break;
                    case SOUTHWEST:
                        points = markMeleeTiles ? (Point[])getSWMeleePoints.apply(xarpusWorldPoint) : (Point[])getSWBoxPoints.apply(xarpusWorldPoint);
                        break;
                    default:
                        return;
                }

                Polygon poly = new Polygon();
                int dangerousPolygonPointsLength = points.length;
                Arrays.stream(points, 0, dangerousPolygonPointsLength).map((point) -> {
                    return this.localToCanvas(dir, point.getX(), point.getY());
                }).filter(Objects::nonNull).forEach((p) -> {
                    poly.addPoint(p.getX(), p.getY());
                });
                this.renderPoly(graphics, this.config.xarpusLineOfSightColor(), poly);
            }
        }

    }

    private Point localToCanvas(Direction dir, int px, int py) {
        LocalPoint lp = LocalPoint.fromWorld(this.client, px, py);
        int x = lp.getX();
        int y = lp.getY();
        int s = 64;
        switch (dir) {
            case NORTHEAST:
                return Perspective.localToCanvas(this.client, new LocalPoint(x - s, y - s), this.client.getPlane());
            case NORTHWEST:
                return Perspective.localToCanvas(this.client, new LocalPoint(x + s, y - s), this.client.getPlane());
            case SOUTHEAST:
                return Perspective.localToCanvas(this.client, new LocalPoint(x - s, y + s), this.client.getPlane());
            case SOUTHWEST:
                return Perspective.localToCanvas(this.client, new LocalPoint(x + s, y + s), this.client.getPlane());
            default:
                return null;
        }
    }
}
