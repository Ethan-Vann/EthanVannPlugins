//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Maiden;

import com.example.theatre.Room;
import com.example.theatre.TheatreConfig;
import com.example.theatre.TheatrePlugin;
import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Maiden extends Room {
    @Inject
    private Client client;
    @Inject
    private MaidenOverlay maidenOverlay;
    private boolean maidenActive;
    private NPC maidenNPC;
    private List<NPC> maidenSpawns = new ArrayList();
    private Map<NPC, Pair<Integer, Integer>> maidenReds = new HashMap();
    private List<WorldPoint> maidenBloodSplatters = new ArrayList();
    private List<WorldPoint> maidenBloodSpawnLocations = new ArrayList();
    private List<WorldPoint> maidenBloodSpawnTrailingLocations = new ArrayList();
    private int ticksUntilAttack = 0;
    private int lastAnimationID = -1;
    private static final int GRAPHICSOBJECT_ID_MAIDEN = 1579;

    @Inject
    protected Maiden(TheatrePlugin plugin, TheatreConfig config) {
        super(plugin, config);
    }

    public void load() {
        this.overlayManager.add(this.maidenOverlay);
    }

    public void unload() {
        this.overlayManager.remove(this.maidenOverlay);
        this.maidenActive = false;
        this.maidenBloodSplatters.clear();
        this.maidenSpawns.clear();
        this.maidenReds.clear();
        this.maidenBloodSpawnLocations.clear();
        this.maidenBloodSpawnTrailingLocations.clear();
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
        NPC npc = npcSpawned.getNpc();
        switch (npc.getId()) {
            case 8360:
            case 8361:
            case 8362:
            case 8363:
            case 8364:
            case 8365:
            case 10814:
            case 10815:
            case 10816:
            case 10817:
            case 10818:
            case 10819:
            case 10822:
            case 10823:
            case 10824:
            case 10825:
            case 10826:
            case 10827:
                this.ticksUntilAttack = 10;
                this.maidenActive = true;
                this.maidenNPC = npc;
                break;
            case 8366:
            case 10820:
            case 10828:
                this.maidenReds.putIfAbsent(npc, new MutablePair(npc.getHealthRatio(), npc.getHealthScale()));
                break;
            case 8367:
            case 10821:
            case 10829:
                this.maidenSpawns.add(npc);
        }

    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        switch (npc.getId()) {
            case 8360:
            case 8361:
            case 8362:
            case 8363:
            case 8364:
            case 8365:
            case 10814:
            case 10815:
            case 10816:
            case 10817:
            case 10818:
            case 10819:
            case 10822:
            case 10823:
            case 10824:
            case 10825:
            case 10826:
            case 10827:
                this.ticksUntilAttack = 0;
                this.maidenActive = false;
                this.maidenSpawns.clear();
                this.maidenNPC = null;
                break;
            case 8366:
            case 10820:
            case 10828:
                this.maidenReds.remove(npc);
                break;
            case 8367:
            case 10821:
            case 10829:
                this.maidenSpawns.remove(npc);
        }

    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.maidenActive) {
            if (this.maidenNPC != null) {
                --this.ticksUntilAttack;
                if (this.lastAnimationID == -1 && this.maidenNPC.getAnimation() != this.lastAnimationID) {
                    this.ticksUntilAttack = 10;
                }

                this.lastAnimationID = this.maidenNPC.getAnimation();
            }

            this.maidenBloodSplatters.clear();
            Iterator var2 = this.client.getGraphicsObjects().iterator();

            while(var2.hasNext()) {
                GraphicsObject graphicsObject = (GraphicsObject)var2.next();
                if (graphicsObject.getId() == 1579) {
                    this.maidenBloodSplatters.add(WorldPoint.fromLocal(this.client, graphicsObject.getLocation()));
                }
            }

            this.maidenBloodSpawnTrailingLocations.clear();
            this.maidenBloodSpawnTrailingLocations.addAll(this.maidenBloodSpawnLocations);
            this.maidenBloodSpawnLocations.clear();
            this.maidenSpawns.forEach((s) -> {
                this.maidenBloodSpawnLocations.add(s.getWorldLocation());
            });
        }
    }

    Color maidenSpecialWarningColor() {
        Color col = Color.GREEN;
        if (this.maidenNPC != null && this.maidenNPC.getInteracting() != null) {
            return this.maidenNPC.getInteracting().getName().equals(this.client.getLocalPlayer().getName()) ? Color.ORANGE : col;
        } else {
            return col;
        }
    }

    public boolean isMaidenActive() {
        return this.maidenActive;
    }

    public NPC getMaidenNPC() {
        return this.maidenNPC;
    }

    public List<NPC> getMaidenSpawns() {
        return this.maidenSpawns;
    }

    public Map<NPC, Pair<Integer, Integer>> getMaidenReds() {
        return this.maidenReds;
    }

    public List<WorldPoint> getMaidenBloodSplatters() {
        return this.maidenBloodSplatters;
    }

    public List<WorldPoint> getMaidenBloodSpawnLocations() {
        return this.maidenBloodSpawnLocations;
    }

    public List<WorldPoint> getMaidenBloodSpawnTrailingLocations() {
        return this.maidenBloodSpawnTrailingLocations;
    }

    public int getTicksUntilAttack() {
        return this.ticksUntilAttack;
    }
}
