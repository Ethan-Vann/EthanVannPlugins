//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Sotetseg;

import com.example.theatre.Room;
import com.example.theatre.TheatreConfig;
import com.example.theatre.TheatrePlugin;
import com.example.theatre.prayer.Prayer;
import com.example.theatre.prayer.TheatrePrayerUtil;
import com.example.theatre.prayer.TheatreUpcomingAttack;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

public class Sotetseg extends Room {
    private static final Logger log = LoggerFactory.getLogger(Sotetseg.class);
    @Inject
    private Client client;
    @Inject
    private TheatrePlugin plugin;
    @Inject
    private SotetsegOverlay sotetsegOverlay;
    @Inject
    private SotetsegPrayerOverlay sotetsegPrayerOverlay;
    private boolean sotetsegActive;
    private NPC sotetsegNPC;
    private LinkedHashSet<Point> redTiles = new LinkedHashSet();
    private HashSet<Point> greenTiles = new HashSet();
    private static final Point swMazeSquareOverWorld = new Point(9, 22);
    private static final Point swMazeSquareUnderWorld = new Point(42, 31);
    private boolean wasInUnderWorld = false;
    private int sotetsegTickCount = -1;
    private boolean offTick = false;
    private boolean bigOrbPresent = false;
    private boolean sotetsegBallCounted = false;
    static final int SOTETSEG_MAGE_ORB = 1606;
    static final int SOTETSEG_RANGE_ORB = 1607;
    static final int SOTETSEG_BIG_AOE_ORB = 1604;
    private static final int GROUNDOBJECT_ID_REDMAZE = 33035;
    private int overWorldRegionID = -1;
    private long lastTick;
    Queue<TheatreUpcomingAttack> upcomingAttackQueue = new PriorityQueue();
    private int attacksLeft = 10;
    private Projectile projectile = null;
    private boolean firstProjectile = true;

    @Inject
    protected Sotetseg(TheatrePlugin plugin, TheatreConfig config) {
        super(plugin, config);
    }

    public void load() {
        this.overlayManager.add(this.sotetsegOverlay);
        this.overlayManager.add(this.sotetsegPrayerOverlay);
    }

    public void unload() {
        this.overlayManager.remove(this.sotetsegOverlay);
        this.overlayManager.remove(this.sotetsegPrayerOverlay);
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
        NPC npc = npcSpawned.getNpc();
        switch (npc.getId()) {
            case 8387:
            case 8388:
            case 10864:
            case 10865:
            case 10867:
            case 10868:
                this.sotetsegActive = true;
                this.sotetsegNPC = npc;
            default:
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        switch (npc.getId()) {
            case 8387:
            case 8388:
            case 10864:
            case 10865:
            case 10867:
            case 10868:
                if (this.client.getPlane() != 3) {
                    this.sotetsegActive = false;
                    this.sotetsegNPC = null;
                    this.upcomingAttackQueue.clear();
                    this.attacksLeft = 10;
                }
            default:
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        Actor actor = event.getActor();
        if (actor instanceof NPC && actor == this.sotetsegNPC) {
            int animation = event.getActor().getAnimation();
            switch (animation) {
                case 8138:
                case 8139:
                    this.sotetsegTickCount = 6;
                    this.upcomingAttackQueue.add(new TheatreUpcomingAttack(this.sotetsegTickCount, Prayer.PROTECT_FROM_MELEE, 1));
            }
        }

    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved projectileSpawned) {
        if (this.sotetsegActive) {
            Projectile p = projectileSpawned.getProjectile();
            if (p != null) {
                if (this.firstProjectile) {
                    this.projectile = projectileSpawned.getProjectile();
                }

                if (this.projectile.getStartCycle() != p.getStartCycle() || this.firstProjectile) {
                    this.projectile = p;
                    TheatreUpcomingAttack upcomingAttack = new TheatreUpcomingAttack(p.getRemainingCycles() / 30, p.getId() == 1606 ? Prayer.PROTECT_FROM_MAGIC : Prayer.PROTECT_FROM_MISSILES);
                    if (p.getInteracting() == this.client.getLocalPlayer() && (p.getId() == 1606 || p.getId() == 1607)) {
                        this.upcomingAttackQueue.add(upcomingAttack);
                    }

                    if (p.getId() == 1604) {
                        this.sotetsegTickCount = 11;
                        this.attacksLeft = 10;
                    }

                    WorldPoint soteWp = WorldPoint.fromLocal(this.client, this.sotetsegNPC.getLocalLocation());
                    WorldPoint projWp = WorldPoint.fromLocal(this.client, p.getX1(), p.getY1(), this.client.getPlane());
                    if (p.getId() == 1606 && this.sotetsegNPC.getAnimation() == 8139 && projWp.equals(soteWp)) {
                        --this.attacksLeft;
                    }

                    if (this.firstProjectile) {
                        this.firstProjectile = false;
                    }

                }
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.sotetsegActive) {
            this.lastTick = System.currentTimeMillis();
            TheatrePrayerUtil.updateNextPrayerQueue(this.getUpcomingAttackQueue());
            if (this.sotetsegNPC != null && (this.sotetsegNPC.getId() == 8388 || this.sotetsegNPC.getId() == 10865 || this.sotetsegNPC.getId() == 10868)) {
                if (this.sotetsegTickCount >= 0) {
                    --this.sotetsegTickCount;
                }

                if (!this.redTiles.isEmpty()) {
                    this.redTiles.clear();
                    this.offTick = false;
                }

                if (!this.greenTiles.isEmpty()) {
                    this.greenTiles.clear();
                }

                if (this.inRoomRegion(TheatrePlugin.SOTETSEG_REGION_OVERWORLD)) {
                    this.wasInUnderWorld = false;
                    if (this.client.getLocalPlayer() != null && this.client.getLocalPlayer().getWorldLocation() != null) {
                        this.overWorldRegionID = this.client.getLocalPlayer().getWorldLocation().getRegionID();
                    }
                }
            }

            if (this.config.sotetsegBigOrbTicks()) {
                boolean foundBigOrb = false;
                Iterator var3 = this.client.getProjectiles().iterator();

                while(var3.hasNext()) {
                    Projectile p = (Projectile)var3.next();
                    if (p.getId() == 1604) {
                        foundBigOrb = true;
                        break;
                    }
                }

                this.bigOrbPresent = foundBigOrb;
            }

            if (!this.bigOrbPresent) {
                this.sotetsegBallCounted = false;
            }

            if (this.bigOrbPresent && !this.sotetsegBallCounted) {
                this.sotetsegTickCount = 10;
                this.sotetsegBallCounted = true;
            }
        }

    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event) {
        if (this.sotetsegActive) {
            GroundObject o = event.getGroundObject();
            if (o.getId() == 33035) {
                Tile t = event.getTile();
                WorldPoint p = WorldPoint.fromLocal(this.client, t.getLocalLocation());
                Point point = new Point(p.getRegionX(), p.getRegionY());
                if (this.inRoomRegion(TheatrePlugin.SOTETSEG_REGION_OVERWORLD)) {
                    this.redTiles.add(new Point(point.getX() - swMazeSquareOverWorld.getX(), point.getY() - swMazeSquareOverWorld.getY()));
                }

                if (this.inRoomRegion(TheatrePlugin.SOTETSEG_REGION_UNDERWORLD)) {
                    this.redTiles.add(new Point(point.getX() - swMazeSquareUnderWorld.getX(), point.getY() - swMazeSquareUnderWorld.getY()));
                    this.wasInUnderWorld = true;
                }
            }
        }

    }

    WorldPoint worldPointFromMazePoint(Point mazePoint) {
        return this.overWorldRegionID == -1 && this.client.getLocalPlayer() != null ? WorldPoint.fromRegion(this.client.getLocalPlayer().getWorldLocation().getRegionID(), mazePoint.getX() + getSwMazeSquareOverWorld().getX(), mazePoint.getY() + getSwMazeSquareOverWorld().getY(), 0) : WorldPoint.fromRegion(this.overWorldRegionID, mazePoint.getX() + getSwMazeSquareOverWorld().getX(), mazePoint.getY() + getSwMazeSquareOverWorld().getY(), 0);
    }

    public boolean isSotetsegActive() {
        return this.sotetsegActive;
    }

    public NPC getSotetsegNPC() {
        return this.sotetsegNPC;
    }

    public LinkedHashSet<Point> getRedTiles() {
        return this.redTiles;
    }

    public HashSet<Point> getGreenTiles() {
        return this.greenTiles;
    }

    public static Point getSwMazeSquareOverWorld() {
        return swMazeSquareOverWorld;
    }

    public static Point getSwMazeSquareUnderWorld() {
        return swMazeSquareUnderWorld;
    }

    public boolean isWasInUnderWorld() {
        return this.wasInUnderWorld;
    }

    public int getSotetsegTickCount() {
        return this.sotetsegTickCount;
    }

    long getLastTick() {
        return this.lastTick;
    }

    Queue<TheatreUpcomingAttack> getUpcomingAttackQueue() {
        return this.upcomingAttackQueue;
    }

    public int getAttacksLeft() {
        return this.attacksLeft;
    }

    public Projectile getProjectile() {
        return this.projectile;
    }

    public boolean isFirstProjectile() {
        return this.firstProjectile;
    }

    public void setFirstProjectile(boolean firstProjectile) {
        this.firstProjectile = firstProjectile;
    }
}
