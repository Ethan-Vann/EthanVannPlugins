//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Bloat;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Ints;
import com.example.theatre.Room;
import com.example.theatre.RoomOverlay;
import com.example.theatre.TheatreConfig;
import com.example.theatre.TheatrePlugin;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Bloat extends Room {
    @Inject
    private Client client;
    @Inject
    private BloatOverlay bloatOverlay;
    private boolean bloatActive;
    private NPC bloatNPC;
    private HashMap<WorldPoint, Integer> bloatHands = new HashMap();
    private int bloatTickCount = -1;
    private int bloatDownCount = 0;
    private int bloatState = 0;
    private boolean bloatStarted;
    public static final Set<Integer> tankObjectIDs = ImmutableSet.of(32957, 32955, 32959, 32960, 32964, 33084, new Integer[]{0});
    public static final Set<Integer> topOfTankObjectIDs = ImmutableSet.of(32958, 32962, 32964, 32965, 33062);
    public static final Set<Integer> ceilingChainsObjectIDs = ImmutableSet.of(32949, 32950, 32951, 32952, 32953, 32954, new Integer[]{32970});

    @Inject
    protected Bloat(Client client, TheatrePlugin plugin, TheatreConfig config) {
        super(plugin, config);
    }

    public void load() {
        this.overlayManager.add(this.bloatOverlay);
    }

    public void unload() {
        this.overlayManager.remove(this.bloatOverlay);
        this.bloatDownCount = 0;
        this.bloatState = 0;
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
        NPC npc = npcSpawned.getNpc();
        switch (npc.getId()) {
            case 8359:
            case 10812:
            case 10813:
                this.bloatActive = true;
                this.bloatNPC = npc;
                this.bloatTickCount = 0;
                this.bloatStarted = false;
            default:
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        switch (npc.getId()) {
            case 8359:
            case 10812:
            case 10813:
                this.bloatActive = false;
                this.bloatNPC = null;
                this.bloatTickCount = -1;
            default:
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged e) {
        if (e.getGameState() == GameState.LOGGED_IN && this.inRoomRegion(TheatrePlugin.BLOAT_REGION)) {
            if (this.config.hideBloatTank()) {
                this.removeGameObjectsFromScene(this.client.getPlane(), Ints.toArray(tankObjectIDs));
                this.removeGameObjectsFromScene(1, Ints.toArray(topOfTankObjectIDs));
            }

            if (this.config.hideCeilingChains()) {
                this.removeGameObjectsFromScene(1, Ints.toArray(ceilingChainsObjectIDs));
            }
        }

    }

    public void removeGameObjectsFromScene(int plane, int... gameObjectIDs) {
        Scene scene = this.client.getScene();
        Tile[][] tiles = scene.getTiles()[plane];

        for(int sceneTilesX = 0; sceneTilesX < 104; ++sceneTilesX) {
            for(int sceneTilesY = 0; sceneTilesY < 104; ++sceneTilesY) {
                Tile tile = tiles[sceneTilesX][sceneTilesY];
                if (tile != null) {
                    GameObject[] gameObjects = tile.getGameObjects();
                    Arrays.stream(gameObjects).filter(Objects::nonNull).filter((gameObject) -> {
                        return Arrays.stream(gameObjectIDs).anyMatch((id) -> {
                            return id == gameObject.getId();
                        });
                    }).forEach(scene::removeGameObject);
                }
            }
        }

    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        if (this.client.getGameState() == GameState.LOGGED_IN && event.getActor() == this.bloatNPC) {
            this.bloatTickCount = 0;
        }
    }

    @Subscribe
    public void onGraphicsObjectCreated(GraphicsObjectCreated graphicsObjectC) {
        if (this.bloatActive) {
            GraphicsObject graphicsObject = graphicsObjectC.getGraphicsObject();
            if (graphicsObject.getId() >= 1560 && graphicsObject.getId() <= 1590) {
                WorldPoint point = WorldPoint.fromLocal(this.client, graphicsObject.getLocation());
                if (!this.bloatHands.containsKey(point)) {
                    this.bloatHands.put(point, 4);
                }
            }
        }

    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.bloatActive) {
            ++this.bloatDownCount;
            ++this.bloatTickCount;
            this.bloatHands.values().removeIf((v) -> {
                return v <= 0;
            });
            this.bloatHands.replaceAll((k, v) -> {
                return v - 1;
            });
            if (this.bloatNPC.getAnimation() == -1) {
                this.bloatDownCount = 0;
                if (this.bloatNPC.getHealthScale() == 0) {
                    this.bloatState = 2;
                } else if (this.bloatTickCount >= 38) {
                    this.bloatState = 4;
                } else {
                    this.bloatState = 1;
                }
            } else if (this.bloatTickCount >= 38) {
                this.bloatState = 4;
            } else if (25 < this.bloatDownCount && this.bloatDownCount < 35) {
                this.bloatState = 3;
            } else if (this.bloatDownCount < 26) {
                this.bloatState = 2;
            } else if (this.bloatNPC.getModelHeight() == 568) {
                this.bloatState = 2;
            } else if (this.bloatTickCount >= 38) {
                this.bloatState = 4;
            } else {
                this.bloatState = 1;
            }
        }

    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        if (this.client.getVarbitValue(6447) == 1 && !this.bloatStarted) {
            this.bloatTickCount = 0;
            this.bloatStarted = true;
        }

    }

    Polygon getBloatTilePoly() {
        if (this.bloatNPC == null) {
            return null;
        } else {
            int size = 1;
            NPCComposition composition = this.bloatNPC.getTransformedComposition();
            if (composition != null) {
                size = composition.getSize();
            }

            LocalPoint lp;
            switch (this.bloatState) {
                case 1:
                case 4:
                    lp = this.bloatNPC.getLocalLocation();
                    if (lp == null) {
                        return null;
                    }

                    return RoomOverlay.getCanvasTileAreaPoly(this.client, lp, size, true);
                case 2:
                case 3:
                    lp = LocalPoint.fromWorld(this.client, this.bloatNPC.getWorldLocation());
                    if (lp == null) {
                        return null;
                    }

                    return RoomOverlay.getCanvasTileAreaPoly(this.client, lp, size, false);
                default:
                    return null;
            }
        }
    }

    Color getBloatStateColor() {
        Color col = this.config.bloatIndicatorColorUP();
        switch (this.bloatState) {
            case 2:
                col = this.config.bloatIndicatorColorDOWN();
                break;
            case 3:
                col = this.config.bloatIndicatorColorWARN();
                break;
            case 4:
                col = this.config.bloatIndicatorColorTHRESH();
        }

        return col;
    }

    public int getBloatDownCount() {
        return this.bloatDownCount;
    }

    public int getBloatState() {
        return this.bloatState;
    }

    public boolean isBloatActive() {
        return this.bloatActive;
    }

    public NPC getBloatNPC() {
        return this.bloatNPC;
    }

    public HashMap<WorldPoint, Integer> getBloatHands() {
        return this.bloatHands;
    }

    public int getBloatTickCount() {
        return this.bloatTickCount;
    }
}
