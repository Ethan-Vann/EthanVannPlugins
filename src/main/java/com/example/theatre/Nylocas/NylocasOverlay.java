//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Nylocas;

import com.google.common.collect.ImmutableList;
import com.example.theatre.RoomOverlay;
import com.example.theatre.TheatreConfig;
import com.example.theatre.TheatreConfig.AGGRESSIVENYLORENDERSTYLE;
import com.example.theatre.TheatreConfig.EXPLOSIVENYLORENDERSTYLE;
import com.example.theatre.TheatreConfig.NYLOTIMEALIVE;
import com.example.theatre.TheatrePlugin;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class NylocasOverlay extends RoomOverlay {
    @Inject
    private Nylocas nylocas;
    private final List<Point> eastSpawnNorthLocalPoints = (new ImmutableList.Builder()).add(new Point(38, 25)).add(new Point(34, 25)).add(new Point(32, 25)).build();
    private final List<Point> eastSpawnSouthLocalPoints = (new ImmutableList.Builder()).add(new Point(38, 24)).add(new Point(34, 24)).add(new Point(32, 24)).build();
    private final List<Point> southSpawnEastLocalPoints = (new ImmutableList.Builder()).add(new Point(24, 9)).add(new Point(24, 14)).add(new Point(24, 16)).build();
    private final List<Point> southSpawnWestLocalPoints = (new ImmutableList.Builder()).add(new Point(23, 9)).add(new Point(23, 14)).add(new Point(23, 16)).build();
    private final List<Point> westSpawnSouthLocalPoints = (new ImmutableList.Builder()).add(new Point(9, 24)).add(new Point(13, 24)).add(new Point(15, 24)).build();
    private final List<Point> westSpawnNorthLocalPoints = (new ImmutableList.Builder()).add(new Point(9, 25)).add(new Point(13, 25)).add(new Point(15, 25)).build();

    @Inject
    protected NylocasOverlay(TheatreConfig config) {
        super(config);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public Dimension render(Graphics2D graphics) {
        Point canvasPoint;
        if (this.nylocas.isInstanceTimerRunning() && this.nylocas.inRoomRegion(TheatrePlugin.NYLOCAS_REGION) && this.config.nyloInstanceTimer()) {
            Player player = this.client.getLocalPlayer();
            if (player != null) {
                canvasPoint = player.getCanvasTextLocation(graphics, "#", player.getLogicalHeight() + 60);
                if (canvasPoint != null) {
                    this.renderTextLocation(graphics, String.valueOf(this.nylocas.getInstanceTimer()), Color.CYAN, canvasPoint);
                }
            }
        }

        if (this.nylocas.isNyloBossAlive()) {
            String text = "";
            if (this.config.nyloBossAttackTickCount() && this.nylocas.getNyloBossAttackTickCount() >= 0) {
                text = text + "[A] " + this.nylocas.getNyloBossAttackTickCount();
                if (this.config.nyloBossSwitchTickCount() || this.config.nyloBossTotalTickCount()) {
                    text = text + " : ";
                }
            }

            if (this.config.nyloBossSwitchTickCount() && this.nylocas.getNyloBossSwitchTickCount() >= 0) {
                text = text + "[S] " + this.nylocas.getNyloBossSwitchTickCount();
                if (this.config.nyloBossTotalTickCount() && this.nylocas.getNyloBossTotalTickCount() >= 0) {
                    text = text + " : ";
                }
            }

            if (this.config.nyloBossTotalTickCount() && this.nylocas.getNyloBossTotalTickCount() >= 0) {
                text = text + "(" + this.nylocas.getNyloBossTotalTickCount() + ")";
            }

            canvasPoint = this.nylocas.getNyloBossNPC().getCanvasTextLocation(graphics, text, 50);
            this.renderTextLocation(graphics, text, Color.WHITE, canvasPoint);
        }

        if (this.nylocas.isNyloActive()) {
            if (this.config.nyloWavesHelper()) {
                String[] nylocasWave = (String[])NylocasWave.wavesHelper.get(this.nylocas.getNyloWave() + 1);
                if (nylocasWave != null) {
                    String eastSpawn = nylocasWave[0];
                    String southSpawn = nylocasWave[1];
                    String westSpawn = nylocasWave[2];
                    String[] eastSpawnSplit = eastSpawn.split("\\|");
                    String[] southSpawnSplit = southSpawn.split("\\|");
                    String[] westSpawnSplit = westSpawn.split("\\|");
                    if (eastSpawnSplit.length > 1) {
                        this.renderNyloHelperOnWalkup(graphics, eastSpawnSplit[0], this.eastSpawnNorthLocalPoints, "east");
                        this.renderNyloHelperOnWalkup(graphics, eastSpawnSplit[1], this.eastSpawnSouthLocalPoints, "east");
                    } else {
                        this.renderNyloHelperOnWalkup(graphics, eastSpawn, this.eastSpawnNorthLocalPoints, "east");
                    }

                    if (southSpawnSplit.length > 1) {
                        this.renderNyloHelperOnWalkup(graphics, southSpawnSplit[0], this.southSpawnEastLocalPoints, "south");
                        this.renderNyloHelperOnWalkup(graphics, southSpawnSplit[1], this.southSpawnWestLocalPoints, "south");
                    } else {
                        this.renderNyloHelperOnWalkup(graphics, southSpawn, this.southSpawnEastLocalPoints, "south");
                    }

                    if (westSpawnSplit.length > 1) {
                        this.renderNyloHelperOnWalkup(graphics, westSpawnSplit[0], this.westSpawnSouthLocalPoints, "west");
                        this.renderNyloHelperOnWalkup(graphics, westSpawnSplit[1], this.westSpawnNorthLocalPoints, "west");
                    } else {
                        this.renderNyloHelperOnWalkup(graphics, westSpawn, this.westSpawnSouthLocalPoints, "west");
                    }
                }
            }

            Polygon westPoly;
            if (this.config.nyloTicksUntilWaves() && !this.nylocas.isNyloBossAlive()) {
                LocalPoint eastPoint = LocalPoint.fromWorld(this.client, WorldPoint.fromRegion(((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getWorldLocation().getRegionID(), 43, 25, this.client.getLocalPlayer().getWorldLocation().getPlane()));
                LocalPoint southPoint = LocalPoint.fromWorld(this.client, WorldPoint.fromRegion(((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getWorldLocation().getRegionID(), 25, 6, this.client.getLocalPlayer().getWorldLocation().getPlane()));
                LocalPoint westPoint = LocalPoint.fromWorld(this.client, WorldPoint.fromRegion(((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getWorldLocation().getRegionID(), 5, 24, this.client.getLocalPlayer().getWorldLocation().getPlane()));
                Polygon southPoly = null;
                Polygon eastPoly = null;
                westPoly = null;
                if (southPoint != null) {
                    southPoly = Perspective.getCanvasTileAreaPoly(this.client, new LocalPoint(southPoint.getX() - 64, southPoint.getY() + 64), 2);
                }

                if (eastPoint != null) {
                    eastPoly = Perspective.getCanvasTileAreaPoly(this.client, new LocalPoint(eastPoint.getX() - 64, eastPoint.getY() - 64), 2);
                }

                if (westPoint != null) {
                    westPoly = Perspective.getCanvasTileAreaPoly(this.client, new LocalPoint(westPoint.getX() + 64, westPoint.getY() + 64), 2);
                }

                if (eastPoly != null) {
                    this.renderTextLocation(graphics, String.valueOf(this.nylocas.getTicksUntilNextWave()), Color.CYAN, this.centerPoint(eastPoly.getBounds()));
                }

                if (southPoly != null) {
                    this.renderTextLocation(graphics, String.valueOf(this.nylocas.getTicksUntilNextWave()), Color.CYAN, this.centerPoint(southPoly.getBounds()));
                }

                if (westPoly != null) {
                    this.renderTextLocation(graphics, String.valueOf(this.nylocas.getTicksUntilNextWave()), Color.CYAN, this.centerPoint(westPoly.getBounds()));
                }
            }

            Iterator var21;
            NPC npc;
            HashMap npcMap;
            int npcSize;
            LocalPoint lp;
            if (this.config.nyloPillars()) {
                npcMap = this.nylocas.getNylocasPillars();
                var21 = npcMap.keySet().iterator();

                while(var21.hasNext()) {
                    npc = (NPC)var21.next();
                    npcSize = (Integer)npcMap.get(npc);
                    String healthStr = npcSize + "%";
                    WorldPoint p = npc.getWorldLocation();
                    lp = LocalPoint.fromWorld(this.client, p.getX() + 1, p.getY() + 1);
                    double rMod = 130.0 * (double)npcSize / 100.0;
                    double gMod = 255.0 * (double)npcSize / 100.0;
                    double bMod = 125.0 * (double)npcSize / 100.0;
                    Color c = new Color((int)(255.0 - rMod), (int)(0.0 + gMod), (int)(0.0 + bMod));
                    if (lp != null) {
                        Point canvasPoint2 = Perspective.localToCanvas(this.client, lp, this.client.getPlane(), 65);
                        this.renderTextLocation(graphics, healthStr, c, canvasPoint2);
                    }
                }
            }

            npcMap = this.nylocas.getNylocasNpcs();
            var21 = npcMap.keySet().iterator();

            while(true) {
                while(true) {
                    String name;
                    do {
                        do {
                            do {
                                if (!var21.hasNext()) {
                                    if (this.config.bigSplits()) {
                                        this.nylocas.getSplitsMap().forEach((lpx, ticks) -> {
                                            Polygon poly = Perspective.getCanvasTileAreaPoly(this.client, lpx, 2);
                                            if (poly != null) {
                                                if (ticks == 1) {
                                                    OverlayUtil.renderPolygon(graphics, poly, this.config.getBigSplitsTileColor1());
                                                }

                                                if (ticks == 2) {
                                                    OverlayUtil.renderPolygon(graphics, poly, this.config.getBigSplitsTileColor2());
                                                }

                                                if (ticks >= 3) {
                                                    OverlayUtil.renderPolygon(graphics, poly, this.config.getBigSplitsHighlightColor());
                                                }
                                            }

                                            Point textLocation = Perspective.getCanvasTextLocation(this.client, graphics, lpx, "#", 0);
                                            if (textLocation != null) {
                                                if (ticks == 1) {
                                                    OverlayUtil.renderTextLocation(graphics, textLocation, Integer.toString(ticks), this.config.getBigSplitsTextColor1());
                                                }

                                                if (ticks == 2) {
                                                    OverlayUtil.renderTextLocation(graphics, textLocation, Integer.toString(ticks), this.config.getBigSplitsTextColor2());
                                                }

                                                if (ticks >= 3) {
                                                    OverlayUtil.renderTextLocation(graphics, textLocation, Integer.toString(ticks), Color.WHITE);
                                                }
                                            }

                                        });
                                    }

                                    return null;
                                }

                                npc = (NPC)var21.next();
                                npcSize = npc.getComposition().getSize();
                                if (this.config.nyloAggressiveOverlay() && this.nylocas.getAggressiveNylocas().contains(npc) && !npc.isDead()) {
                                    if (this.config.nyloAggressiveOverlayStyle() == AGGRESSIVENYLORENDERSTYLE.TILE) {
                                        LocalPoint lp3 = npc.getLocalLocation();
                                        if (lp3 != null) {
                                            westPoly = getCanvasTileAreaPoly(this.client, lp3, npcSize, -25);
                                            this.renderPoly(graphics, Color.RED, westPoly, 1);
                                        }
                                    } else if (this.config.nyloAggressiveOverlayStyle() == AGGRESSIVENYLORENDERSTYLE.HULL) {
                                        Shape objectClickbox = npc.getConvexHull();
                                        if (objectClickbox != null) {
                                            Color color = Color.RED;
                                            graphics.setColor(color);
                                            graphics.setStroke(new BasicStroke(2.0F));
                                            graphics.draw(objectClickbox);
                                            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                                            graphics.fill(objectClickbox);
                                        }
                                    }
                                }

                                int ticksLeft = (Integer)npcMap.get(npc);
                                if (ticksLeft > -1 && ticksLeft <= this.config.nyloExplosionDisplayTicks()) {
                                    if (this.config.nyloTimeAlive() && !npc.isDead()) {
                                        int ticksAlive = ticksLeft;
                                        if (this.config.nyloTimeAliveCountStyle() == NYLOTIMEALIVE.COUNTUP) {
                                            ticksAlive = 52 - ticksLeft;
                                        }

                                        Point textLocation = npc.getCanvasTextLocation(graphics, String.valueOf(ticksAlive), 60);
                                        if (textLocation != null) {
                                            if (this.config.nyloExplosionOverlayStyle() == EXPLOSIVENYLORENDERSTYLE.RECOLOR_TICK && this.config.nyloExplosions() && ticksLeft <= 6) {
                                                this.renderTextLocation(graphics, String.valueOf(ticksAlive), Color.RED, textLocation);
                                            } else {
                                                this.renderTextLocation(graphics, String.valueOf(ticksAlive), Color.WHITE, textLocation);
                                            }
                                        }
                                    }

                                    if (this.config.nyloExplosions() && ticksLeft <= 6 && this.config.nyloExplosionOverlayStyle() == EXPLOSIVENYLORENDERSTYLE.TILE) {
                                        LocalPoint lp2 = npc.getLocalLocation();
                                        if (lp2 != null) {
                                            this.renderPoly(graphics, Color.YELLOW, getCanvasTileAreaPoly(this.client, lp2, npcSize, -15), 1);
                                        }
                                    }
                                }

                                name = npc.getName();
                            } while(!this.config.nyloHighlightOverlay());
                        } while(npc.isDead());

                        lp = npc.getLocalLocation();
                    } while(lp == null);

                    if (this.config.getHighlightMeleeNylo() && "Nylocas Ischyros".equals(name)) {
                        this.renderPoly(graphics, new Color(255, 188, 188), Perspective.getCanvasTileAreaPoly(this.client, lp, npcSize), 1);
                    } else if (this.config.getHighlightRangeNylo() && "Nylocas Toxobolos".equals(name)) {
                        this.renderPoly(graphics, Color.GREEN, Perspective.getCanvasTileAreaPoly(this.client, lp, npcSize), 1);
                    } else if (this.config.getHighlightMageNylo() && "Nylocas Hagios".equals(name)) {
                        this.renderPoly(graphics, Color.CYAN, Perspective.getCanvasTileAreaPoly(this.client, lp, npcSize), 1);
                    }
                }
            }
        } else {
            return null;
        }
    }

    private void renderNyloHelperOnWalkup(Graphics2D graphics, String nyloHelperString, List<Point> pointArray, String direction) {
        if (!pointArray.isEmpty()) {
            String[] nyloSpawnSplitCsv = nyloHelperString.split("-");
            if (nyloSpawnSplitCsv.length > 1) {
                for(int i = 0; i < nyloSpawnSplitCsv.length; ++i) {
                    this.drawPoly(graphics, nyloSpawnSplitCsv[i], direction, LocalPoint.fromWorld(this.client, WorldPoint.fromRegion(((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getWorldLocation().getRegionID(), ((Point)pointArray.get(i)).getX(), ((Point)pointArray.get(i)).getY(), this.client.getLocalPlayer().getWorldLocation().getPlane())));
                }
            } else if (!nyloHelperString.isEmpty()) {
                this.drawPoly(graphics, nyloHelperString, direction, LocalPoint.fromWorld(this.client, WorldPoint.fromRegion(((Player)Objects.requireNonNull(this.client.getLocalPlayer())).getWorldLocation().getRegionID(), ((Point)pointArray.get(0)).getX(), ((Point)pointArray.get(0)).getY(), this.client.getLocalPlayer().getWorldLocation().getPlane())));
            }

        }
    }

    private void drawPoly(Graphics2D graphics, String nyloType, String direction, LocalPoint localPoint) {
        Polygon poly = null;
        if (!nyloType.equals("mage") && !nyloType.equals("melee") && !nyloType.equals("range")) {
            LocalPoint localPointBig = null;
            switch (direction) {
                case "east":
                    localPointBig = new LocalPoint(localPoint.getX() - 64, localPoint.getY() - 64);
                    break;
                case "west":
                    localPointBig = new LocalPoint(localPoint.getX() + 64, localPoint.getY() + 64);
                    break;
                case "south":
                    localPointBig = new LocalPoint(localPoint.getX() - 64, localPoint.getY() + 64);
            }

            if (localPointBig != null) {
                poly = Perspective.getCanvasTileAreaPoly(this.client, localPointBig, 2);
            }
        } else {
            poly = Perspective.getCanvasTilePoly(this.client, localPoint);
        }

        if (poly != null) {
            this.renderPolyWithFillAlpha(graphics, this.getColor(nyloType), poly, 2, 60);
            this.renderTextLocation(graphics, String.valueOf(this.nylocas.getNyloWave() + 1), Color.YELLOW, this.centerPoint(poly.getBounds()));
        }

    }

    private Point centerPoint(Rectangle rect) {
        int x = (int)(rect.getX() + rect.getWidth() / 2.0);
        int y = (int)(rect.getY() + rect.getHeight() / 2.0);
        return new Point(x, y);
    }

    private Color getColor(String nyloType) {
        if (nyloType.equalsIgnoreCase("melee")) {
            return Color.BLACK;
        } else if (nyloType.equalsIgnoreCase("range")) {
            return Color.GREEN;
        } else {
            return nyloType.equalsIgnoreCase("mage") ? Color.CYAN : Color.BLACK;
        }
    }
}
