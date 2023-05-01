//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Xarpus;

import com.google.common.collect.ImmutableSet;
import com.example.theatre.Room;
import com.example.theatre.TheatreConfig;
import com.example.theatre.TheatreConfig.XARPUS_EXHUMED_COUNT;
import com.example.theatre.TheatrePlugin;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.infobox.Counter;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ImageUtil;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.util.*;

public class Xarpus extends Room {
    @Inject
    private Client client;
    @Inject
    private XarpusOverlay xarpusOverlay;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private TheatrePlugin p;
    private boolean xarpusActive;
    private boolean xarpusStarted = false;
    private NPC xarpusNPC;
    private int instanceTimer = 0;
    private boolean isInstanceTimerRunning = false;
    private boolean nextInstance = true;
    private boolean exhumedSpawned = false;
    private final Map<Long, Pair<GroundObject, Integer>> xarpusExhumeds = new HashMap();
    private Counter exhumedCounter;
    private int exhumedCount;
    private int xarpusTicksUntilAttack;
    private boolean postScreech = false;
    private boolean xarpusStare;
    private static BufferedImage EXHUMED_COUNT_ICON;
    private static final int GROUNDOBJECT_ID_EXHUMED = 32743;
    private boolean isHM;
    private static final Set<Integer> XARPUS_HM_ID = ImmutableSet.of(10770, 10771, 10772, 10773);

    @Inject
    protected Xarpus(TheatrePlugin plugin, TheatreConfig config) {
        super(plugin, config);
    }

    public void init() {
        EXHUMED_COUNT_ICON = ImageUtil.resizeCanvas(ImageUtil.getResourceStreamFromClass(TheatrePlugin.class, "1067-POISON.png"), 26, 26);
    }

    public void load() {
        this.overlayManager.add(this.xarpusOverlay);
    }

    public void unload() {
        this.overlayManager.remove(this.xarpusOverlay);
        this.infoBoxManager.removeInfoBox(this.exhumedCounter);
        this.exhumedCounter = null;
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
        NPC npc = npcSpawned.getNpc();
        switch (npc.getId()) {
            case 8338:
            case 8339:
            case 8340:
            case 8341:
            case 10766:
            case 10767:
            case 10768:
            case 10769:
            case 10770:
            case 10771:
            case 10772:
            case 10773:
                this.isHM = XARPUS_HM_ID.contains(npc.getId());
                this.xarpusActive = true;
                this.xarpusNPC = npc;
                this.xarpusStare = false;
                this.xarpusTicksUntilAttack = 9;
                this.exhumedSpawned = false;
                this.postScreech = false;
            default:
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        switch (npc.getId()) {
            case 8338:
            case 8339:
            case 8340:
            case 8341:
            case 10766:
            case 10767:
            case 10768:
            case 10769:
            case 10770:
            case 10771:
            case 10772:
            case 10773:
                this.isHM = false;
                this.xarpusActive = false;
                this.xarpusNPC = null;
                this.xarpusStare = false;
                this.xarpusTicksUntilAttack = 9;
                this.xarpusExhumeds.clear();
                this.infoBoxManager.removeInfoBox(this.exhumedCounter);
                this.exhumedCounter = null;
                this.isInstanceTimerRunning = false;
                this.exhumedSpawned = false;
                this.postScreech = false;
                this.exhumedCount = -1;
            default:
        }
    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event) {
        if (this.xarpusActive) {
            GroundObject o = event.getGroundObject();
            if (o.getId() == 32743) {
                long hash = o.getHash();
                if (this.xarpusExhumeds.containsKey(hash)) {
                    return;
                }

                this.exhumedSpawned = true;
                if (this.exhumedCounter == null) {
                    switch (TheatrePlugin.partySize) {
                        case 2:
                            this.exhumedCount = this.isHM ? 13 : 9;
                            break;
                        case 3:
                            this.exhumedCount = this.isHM ? 16 : 12;
                            break;
                        case 4:
                            this.exhumedCount = this.isHM ? 20 : 15;
                            break;
                        case 5:
                            this.exhumedCount = this.isHM ? 24 : 18;
                            break;
                        default:
                            this.exhumedCount = this.isHM ? 9 : 7;
                    }

                    if (this.config.xarpusExhumedCount() != XARPUS_EXHUMED_COUNT.OFF) {
                        this.exhumedCounter = new Counter(EXHUMED_COUNT_ICON, this.p, this.config.xarpusExhumedCount() == XARPUS_EXHUMED_COUNT.DOWN ? this.exhumedCount - 1 : 1);
                        this.infoBoxManager.addInfoBox(this.exhumedCounter);
                    }
                } else {
                    this.exhumedCounter.setCount(this.config.xarpusExhumedCount() == XARPUS_EXHUMED_COUNT.DOWN ? this.exhumedCounter.getCount() - 1 : this.exhumedCounter.getCount() + 1);
                }

                this.xarpusExhumeds.put(hash, Pair.of(o, this.isHM ? 9 : 11));
            }
        }

    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        if (!this.xarpusStarted && this.inRoomRegion(TheatrePlugin.XARPUS_REGION) && this.client.getVarbitValue(this.client.getVarps(), 6447) == 2 && this.client.getVarbitValue(4605) == 1) {
            this.xarpusStarted = true;
            this.isInstanceTimerRunning = false;
        }

    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.xarpusActive) {
            if (!this.xarpusExhumeds.isEmpty()) {
                this.xarpusExhumeds.replaceAll((k, v) -> {
                    return Pair.of(v.getLeft(), (Integer)v.getRight() - 1);
                });
                this.xarpusExhumeds.values().removeIf((p) -> {
                    return (Integer)p.getRight() <= 0;
                });
            }

            if (this.xarpusNPC.getOverheadText() != null && !this.xarpusStare) {
                this.xarpusStare = true;
                this.xarpusTicksUntilAttack = 9;
            }

            if (this.xarpusStare) {
                --this.xarpusTicksUntilAttack;
                if (this.xarpusTicksUntilAttack <= 0) {
                    if (!this.postScreech) {
                        this.postScreech = true;
                    }

                    this.xarpusTicksUntilAttack = 8;
                }
            } else if (this.xarpusNPC.getId() == 8340 || this.xarpusNPC.getId() == 10768 || this.xarpusNPC.getId() == 10772) {
                --this.xarpusTicksUntilAttack;
                if (this.xarpusTicksUntilAttack <= 0) {
                    this.xarpusTicksUntilAttack = 4;
                }
            }
        }

        if (this.isInstanceTimerRunning) {
            this.instanceTimer = (this.instanceTimer + 1) % 4;
        }

    }

    @Subscribe
    public void onClientTick(ClientTick event) {
        if (this.client.getLocalPlayer() != null) {
            List<Player> players = this.client.getPlayers();
            Iterator var3 = players.iterator();

            while(true) {
                Point point;
                do {
                    do {
                        do {
                            LocalPoint lpPlayer;
                            LocalPoint lpChest;
                            do {
                                Player player;
                                do {
                                    if (!var3.hasNext()) {
                                        return;
                                    }

                                    player = (Player)var3.next();
                                } while(player.getWorldLocation() == null);

                                WorldPoint wpPlayer = player.getWorldLocation();
                                lpPlayer = LocalPoint.fromWorld(this.client, wpPlayer.getX(), wpPlayer.getY());
                                WorldPoint wpChest = WorldPoint.fromRegion(player.getWorldLocation().getRegionID(), 17, 5, player.getWorldLocation().getPlane());
                                lpChest = LocalPoint.fromWorld(this.client, wpChest.getX(), wpChest.getY());
                            } while(lpChest == null);

                            point = new Point(lpChest.getSceneX() - lpPlayer.getSceneX(), lpChest.getSceneY() - lpPlayer.getSceneY());
                        } while(!this.isInSotetsegRegion());
                    } while(point.getY() != 1);
                } while(point.getX() != 1 && point.getX() != 2 && point.getX() != 3);

                if (this.nextInstance) {
                    this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Xarpus instance timer started", "");
                    this.instanceTimer = 2;
                    this.isInstanceTimerRunning = true;
                    this.nextInstance = false;
                }
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            this.nextInstance = true;
        }

    }

    boolean isInSotetsegRegion() {
        return this.inRoomRegion(TheatrePlugin.SOTETSEG_REGION_OVERWORLD) || this.inRoomRegion(TheatrePlugin.SOTETSEG_REGION_UNDERWORLD);
    }

    public boolean isXarpusActive() {
        return this.xarpusActive;
    }

    public NPC getXarpusNPC() {
        return this.xarpusNPC;
    }

    public int getInstanceTimer() {
        return this.instanceTimer;
    }

    public boolean isInstanceTimerRunning() {
        return this.isInstanceTimerRunning;
    }

    public boolean isExhumedSpawned() {
        return this.exhumedSpawned;
    }

    public Map<Long, Pair<GroundObject, Integer>> getXarpusExhumeds() {
        return this.xarpusExhumeds;
    }

    public Counter getExhumedCounter() {
        return this.exhumedCounter;
    }

    public int getXarpusTicksUntilAttack() {
        return this.xarpusTicksUntilAttack;
    }

    public boolean isPostScreech() {
        return this.postScreech;
    }

    public boolean isHM() {
        return this.isHM;
    }
}
