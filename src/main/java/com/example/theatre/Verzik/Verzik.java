//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Verzik;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.example.theatre.Room;
import com.example.theatre.TheatreConfig;
import com.example.theatre.TheatrePlugin;
import com.example.theatre.prayer.Prayer;
import com.example.theatre.prayer.TheatrePrayerUtil;
import com.example.theatre.prayer.TheatreUpcomingAttack;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Verzik extends Room {
    @Inject
    private Client client;
    @Inject
    private VerzikOverlay verzikOverlay;
    @Inject
    private VerzikPrayerOverlay verzikPrayerOverlay;
    private boolean verzikActive;
    private NPC verzikNPC;
    private Projectile projectile = null;
    private boolean firstProjectile = true;
    private Phase verzikPhase;
    private SpecialAttack verzikSpecial;
    private int verzikYellows;
    private Map<Projectile, WorldPoint> verzikRangeProjectiles;
    private HashSet<NPC> verzikAggros;
    private Map<NPC, Pair<Integer, Integer>> verzikReds;
    private NPC verzikLocalTornado;
    private HashSet<MemorizedTornado> verzikTornadoes;
    private WorldPoint lastPlayerLocation0;
    private WorldPoint lastPlayerLocation1;
    private int verzikLightningAttacks;
    private final List<Projectile> verzikRangedAttacks;
    private final Predicate<Projectile> isValidVerzikAttack;
    private static final Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
    private int verzikTicksUntilAttack;
    private int verzikTotalTicksUntilAttack;
    private int verzikAttackCount;
    private long lastTick;
    Queue<TheatreUpcomingAttack> upcomingAttackQueue;
    Set<VerzikPoisonTile> verzikPoisonTiles;
    private boolean verzikEnraged;
    private boolean verzikFirstEnraged;
    private boolean verzikTickPaused;
    private boolean verzikRedPhase;
    private int verzikLastAnimation;
    private static final int NPC_ID_TORNADO = 8386;
    private static final int VERZIK_RANGE_BALL = 1583;
    private static final int VERZIK_LIGHTNING_BALL = 1585;
    private static final int VERZIK_P1_MAGIC = 8109;
    private static final int VERZIK_P2_REG = 8114;
    private static final int VERZIK_P2_BOUNCE = 8116;
    private static final int VERZIK_P2_POISON = 8116;
    private static final int VERZIK_BEGIN_REDS = 8117;
    private static final int VERZIK_P3_MAGE = 8124;
    private static final int VERZIK_P3_MAGE_PROJECTILE = 1594;
    private static final int VERZIK_P3_RANGE = 8125;
    private static final int VERZIK_P3_RANGE_PROJECTILE = 1593;
    private static final int VERZIK_P3_ATTACK_TICKS = 3;
    private static final int P3_CRAB_ATTACK_COUNT = 5;
    private static final int P3_WEB_ATTACK_COUNT = 10;
    private static final int P3_YELLOW_ATTACK_COUNT = 15;
    private static final int P3_GREEN_ATTACK_COUNT = 20;
    private Set<Integer> WEAPON_SET;
    private static final Set<Integer> HELMET_SET = ImmutableSet.of(12931, 13197, 13199);
    private boolean isHM;
    private static final Set<Integer> VERZIK_HM_ID = ImmutableSet.of(10847, 10848, 10849, 10850, 10851, 10852, new Integer[]{10853});
    private boolean verzikHardmodeSeenYellows;

    @Inject
    private Verzik(TheatrePlugin plugin, TheatreConfig config) {
        super(plugin, config);
        this.verzikSpecial = SpecialAttack.NONE;
        this.verzikRangeProjectiles = new HashMap();
        this.verzikAggros = new HashSet();
        this.verzikReds = new HashMap();
        this.verzikLocalTornado = null;
        this.verzikTornadoes = new HashSet();
        this.verzikLightningAttacks = 4;
        this.verzikRangedAttacks = new ArrayList();
        this.isValidVerzikAttack = (p) -> {
            return p.getRemainingCycles() > 0 && (p.getId() == 1583 || p.getId() == 1585);
        };
        this.verzikTicksUntilAttack = -1;
        this.verzikTotalTicksUntilAttack = 0;
        this.upcomingAttackQueue = new PriorityQueue();
        this.verzikPoisonTiles = new HashSet();
        this.verzikEnraged = false;
        this.verzikFirstEnraged = false;
        this.verzikTickPaused = true;
        this.verzikRedPhase = false;
        this.verzikLastAnimation = -1;
        this.verzikHardmodeSeenYellows = false;
    }

    public void load() {
        this.updateConfig();
        this.overlayManager.add(this.verzikOverlay);
        this.overlayManager.add(this.verzikPrayerOverlay);
    }

    public void unload() {
        this.overlayManager.remove(this.verzikOverlay);
        this.overlayManager.remove(this.verzikPrayerOverlay);
        this.verzikCleanup();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged change) {
        if (change.getKey().equals("weaponSet")) {
            this.updateConfig();
        }

    }

    private void updateConfig() {
        this.WEAPON_SET = new HashSet();
        Iterator var1 = COMMA_SPLITTER.split(this.config.weaponSet()).iterator();

        while(var1.hasNext()) {
            String s = (String)var1.next();

            try {
                this.WEAPON_SET.add(Integer.parseInt(s));
            } catch (NumberFormatException var4) {
            }
        }

    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
        NPC npc = npcSpawned.getNpc();
        switch (npc.getId()) {
            case 8369:
            case 10830:
            case 10847:
                this.verzikSpawn(npc);
                break;
            case 8370:
            case 10831:
            case 10848:
                this.verzikPhase = Phase.PHASE1;
                this.verzikSpawn(npc);
                break;
            case 8371:
            case 10832:
            case 10849:
                this.verzikSpawn(npc);
                break;
            case 8372:
            case 10833:
            case 10850:
                this.verzikPhase = Phase.PHASE2;
                this.verzikSpawn(npc);
                break;
            case 8373:
            case 10834:
            case 10851:
                this.verzikSpawn(npc);
                break;
            case 8374:
            case 10835:
            case 10852:
                this.verzikRangeProjectiles.clear();
                this.verzikPhase = Phase.PHASE3;
                this.verzikSpawn(npc);
                break;
            case 8375:
            case 10836:
            case 10853:
                this.verzikSpawn(npc);
                break;
            case 8376:
            case 10837:
            case 10854:
                if (this.verzikNPC != null && this.verzikNPC.getInteracting() == null) {
                    this.verzikSpecial = SpecialAttack.WEBS;
                }
                break;
            case 8381:
            case 8382:
            case 8383:
            case 10858:
            case 10859:
            case 10860:
                this.verzikAggros.add(npc);
                break;
            case 8385:
            case 10845:
            case 10862:
                this.verzikReds.putIfAbsent(npc, new MutablePair(npc.getHealthRatio(), npc.getHealthScale()));
                break;
            case 8386:
            case 10846:
            case 10863:
                if (this.verzikLocalTornado == null) {
                    this.verzikTornadoes.add(new MemorizedTornado(npc));
                }

                if (!this.verzikEnraged) {
                    this.verzikEnraged = true;
                    this.verzikFirstEnraged = true;
                }
        }

    }

    @Subscribe
    public void onGameObjectSpawn(GameObjectSpawned gameObject) {
        if (this.verzikActive && this.verzikPhase == Phase.PHASE2) {
            this.verzikPoisonTiles.add(new VerzikPoisonTile(WorldPoint.fromLocal(this.client, gameObject.getTile().getLocalLocation())));
        }

    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        switch (npc.getId()) {
            case 8369:
            case 8370:
            case 8371:
            case 8372:
            case 8373:
            case 8374:
            case 8375:
            case 10830:
            case 10831:
            case 10832:
            case 10833:
            case 10834:
            case 10835:
            case 10836:
            case 10847:
            case 10848:
            case 10849:
            case 10850:
            case 10851:
            case 10852:
            case 10853:
                this.verzikCleanup();
                break;
            case 8381:
            case 8382:
            case 8383:
            case 10858:
            case 10859:
            case 10860:
                this.verzikAggros.remove(npc);
                break;
            case 8385:
            case 10845:
            case 10862:
                this.verzikReds.remove(npc);
                break;
            case 8386:
            case 10846:
            case 10863:
                this.verzikTornadoes.remove(npc);
                if (this.verzikLocalTornado == npc) {
                    this.verzikLocalTornado = null;
                }
        }

    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded entry) {
        if (this.config.purpleCrabAttackMES() && this.verzikNPC != null && (this.verzikNPC.getId() == 8372 || this.verzikNPC.getId() == 10833 || this.verzikNPC.getId() == 10850) && entry.getTarget().contains("Nylocas Athanatos") && entry.getMenuEntry().getType() == MenuAction.NPC_SECOND_OPTION) {
            Player player = this.client.getLocalPlayer();
            PlayerComposition playerComp = player != null ? player.getPlayerComposition() : null;
            if (playerComp == null || this.WEAPON_SET.contains(playerComp.getEquipmentId(KitType.WEAPON)) || HELMET_SET.contains(playerComp.getEquipmentId(KitType.HEAD))) {
                return;
            }

            this.removeUnwantedNPCs("Nylocas Athanatos");
        }

    }

    private void removeUnwantedNPCs(String name) {
        MenuEntry[] oldEntries = this.client.getMenuEntries();
        MenuEntry[] newEntries = (MenuEntry[])Arrays.stream(oldEntries).filter((e) -> {
            NPC npc = e.getNpc();
            return npc == null || Objects.equals(npc.getName(), name);
        }).toArray((x$0) -> {
            return new MenuEntry[x$0];
        });
        if (oldEntries.length != newEntries.length) {
            this.client.setMenuEntries(newEntries);
        }

    }

    public void projectilSpawned(ProjectileMoved event) {
        if (this.verzikActive && this.verzikPhase == Phase.PHASE3) {
            Projectile p = event.getProjectile();
            if (p != null) {
                this.projectile = p;
                if (p.getInteracting() == this.client.getLocalPlayer() && (p.getId() == 1593 || p.getId() == 1594)) {
                    TheatreUpcomingAttack upcomingAttack = new TheatreUpcomingAttack(p.getRemainingCycles() / 30, p.getId() == 1594 ? Prayer.PROTECT_FROM_MAGIC : Prayer.PROTECT_FROM_MISSILES);
                    this.upcomingAttackQueue.add(upcomingAttack);
                }

            }
        }
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        if (this.firstProjectile) {
            this.projectile = event.getProjectile();
            this.projectilSpawned(event);
            this.firstProjectile = false;
        }

        if (this.projectile.getStartCycle() != event.getProjectile().getStartCycle()) {
            this.projectilSpawned(event);
        }

        if (event.getProjectile().getId() == 1583) {
            this.verzikRangeProjectiles.put(event.getProjectile(), WorldPoint.fromLocal(this.client, event.getPosition()));
        }

    }

    private void handleVerzikAttacks(Projectile p) {
        int id = p.getId();
        switch (id) {
            case 1583:
                if (!this.verzikRangedAttacks.contains(p)) {
                    this.verzikRangedAttacks.add(p);
                    --this.verzikLightningAttacks;
                }
                break;
            case 1585:
                if (!this.verzikRangedAttacks.contains(p)) {
                    this.verzikRangedAttacks.add(p);
                    this.verzikLightningAttacks = 4;
                }
        }

    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.verzikActive) {
            this.lastTick = System.currentTimeMillis();
            TheatrePrayerUtil.updateNextPrayerQueue(this.getUpcomingAttackQueue());
            Iterator iterator;
            if (this.verzikPhase == Phase.PHASE2) {
                Projectile projectile;
                if (this.verzikNPC.getId() == 8372 || this.verzikNPC.getId() == 10833 || this.verzikNPC.getId() == 10850) {
                    iterator = this.client.getProjectiles().iterator();

                    label192: {
                        do {
                            do {
                                if (!iterator.hasNext()) {
                                    break label192;
                                }

                                projectile = (Projectile)iterator.next();
                            } while(projectile.getRemainingCycles() <= 0);
                        } while(projectile.getId() != 1583 && projectile.getId() != 1585);

                        this.handleVerzikAttacks(projectile);
                    }

                    this.verzikRangedAttacks.removeIf((p) -> {
                        return p.getRemainingCycles() <= 0;
                    });
                }

                if (!this.verzikRangeProjectiles.isEmpty()) {
                    iterator = this.verzikRangeProjectiles.keySet().iterator();

                    while(iterator.hasNext()) {
                        projectile = (Projectile)iterator.next();
                        if (projectile.getRemainingCycles() < 1) {
                            iterator.remove();
                        }
                    }
                }

                if (this.isHM) {
                    VerzikPoisonTile.updateTiles(this.verzikPoisonTiles);
                }
            }

            if (this.verzikPhase == Phase.PHASE3 && !this.verzikTornadoes.isEmpty()) {
                if (this.lastPlayerLocation1 != null) {
                    iterator = this.verzikTornadoes.iterator();

                    while(iterator.hasNext()) {
                        MemorizedTornado tornado = (MemorizedTornado)iterator.next();
                        WorldPoint tornadoLocation = tornado.getNpc().getWorldLocation();
                        if (tornado.getCurrentPosition() == null) {
                            tornado.setCurrentPosition(tornadoLocation);
                        } else {
                            tornado.setLastPosition(tornado.getCurrentPosition());
                            tornado.setCurrentPosition(tornadoLocation);
                        }
                    }
                }

                if (this.lastPlayerLocation1 == null) {
                    this.lastPlayerLocation1 = this.client.getLocalPlayer().getWorldLocation();
                    this.lastPlayerLocation0 = this.lastPlayerLocation1;
                } else {
                    this.lastPlayerLocation1 = this.lastPlayerLocation0;
                    this.lastPlayerLocation0 = this.client.getLocalPlayer().getWorldLocation();
                    this.verzikTornadoes.removeIf((entry) -> {
                        return entry.getRelativeDelta(this.lastPlayerLocation1) != -1;
                    });
                }

                if (this.verzikTornadoes.size() == 1 && this.verzikLocalTornado == null) {
                    this.verzikTornadoes.forEach((tornadox) -> {
                        this.verzikLocalTornado = tornadox.getNpc();
                    });
                }
            }

            Function<Integer, Integer> adjust_for_enrage = (i) -> {
                return this.isVerzikEnraged() ? i - 2 : i;
            };
            if (this.verzikPhase == Phase.PHASE3) {
                if (this.verzikYellows != 0) {
                    --this.verzikYellows;
                } else {
                    boolean foundYellow = false;
                    Iterator var9 = this.client.getGraphicsObjects().iterator();

                    while(var9.hasNext()) {
                        GraphicsObject object = (GraphicsObject)var9.next();
                        if (object.getId() == 1595) {
                            this.verzikYellows = this.verzikHardmodeSeenYellows ? 2 : 14;
                            this.verzikHardmodeSeenYellows = true;
                            foundYellow = true;
                            break;
                        }
                    }

                    if (!foundYellow) {
                        this.verzikHardmodeSeenYellows = false;
                    }
                }
            }

            if (this.verzikTickPaused) {
                switch (this.verzikNPC.getId()) {
                    case 8370:
                    case 10831:
                    case 10848:
                        this.verzikPhase = Phase.PHASE1;
                        this.verzikAttackCount = 0;
                        this.verzikTicksUntilAttack = 18;
                        this.verzikTickPaused = false;
                        break;
                    case 8372:
                    case 10833:
                    case 10850:
                        this.verzikPhase = Phase.PHASE2;
                        this.verzikAttackCount = 0;
                        this.verzikTicksUntilAttack = 3;
                        this.verzikTickPaused = false;
                        break;
                    case 8374:
                    case 10835:
                    case 10852:
                        this.verzikPhase = Phase.PHASE3;
                        this.verzikAttackCount = 0;
                        this.verzikTicksUntilAttack = 6;
                        this.verzikTickPaused = false;
                }
            } else if (this.verzikSpecial == SpecialAttack.WEBS) {
                ++this.verzikTotalTicksUntilAttack;
                if (this.verzikNPC.getInteracting() != null) {
                    this.verzikSpecial = SpecialAttack.WEB_COOLDOWN;
                    this.verzikAttackCount = 10;
                    this.verzikTicksUntilAttack = 10;
                    this.verzikFirstEnraged = false;
                }
            } else {
                this.verzikTicksUntilAttack = Math.max(0, this.verzikTicksUntilAttack - 1);
                ++this.verzikTotalTicksUntilAttack;
                int animationID = this.verzikNPC.getAnimation();
                if (animationID > -1 && this.verzikPhase == Phase.PHASE1 && this.verzikTicksUntilAttack < 5 && animationID != this.verzikLastAnimation && animationID == 8109) {
                    this.verzikTicksUntilAttack = 14;
                    ++this.verzikAttackCount;
                }

                if (animationID > -1 && this.verzikPhase == Phase.PHASE2 && this.verzikTicksUntilAttack < 3 && animationID != this.verzikLastAnimation) {
                    switch (animationID) {
                        case 8114:
                        case 8116:
                            this.verzikTicksUntilAttack = 4;
                            ++this.verzikAttackCount;
                            if (this.verzikAttackCount == 7 && this.verzikRedPhase) {
                                this.verzikTicksUntilAttack = 8;
                            }
                        case 8115:
                        default:
                            break;
                        case 8117:
                            this.verzikRedPhase = true;
                            this.verzikAttackCount = 0;
                            this.verzikTicksUntilAttack = 12;
                    }
                }

                this.verzikLastAnimation = animationID;
                if (this.verzikPhase == Phase.PHASE3) {
                    this.verzikAttackCount %= 20;
                    if (this.verzikTicksUntilAttack <= 0) {
                        ++this.verzikAttackCount;
                        if (this.verzikAttackCount < 10) {
                            this.verzikSpecial = SpecialAttack.NONE;
                            this.verzikTicksUntilAttack = (Integer)adjust_for_enrage.apply(7);
                        } else if (this.verzikAttackCount < 15) {
                            this.verzikSpecial = SpecialAttack.NONE;
                            this.verzikTicksUntilAttack = (Integer)adjust_for_enrage.apply(7);
                        } else if (this.verzikAttackCount < 16) {
                            this.verzikSpecial = SpecialAttack.YELLOWS;
                            if (this.isHM) {
                                this.verzikTicksUntilAttack = 27;
                            } else {
                                this.verzikTicksUntilAttack = 21;
                            }
                        } else if (this.verzikAttackCount < 20) {
                            this.verzikSpecial = SpecialAttack.NONE;
                            this.verzikTicksUntilAttack = (Integer)adjust_for_enrage.apply(7);
                        } else if (this.verzikAttackCount < 21) {
                            this.verzikSpecial = SpecialAttack.GREEN;
                            this.verzikTicksUntilAttack = 12;
                        } else {
                            this.verzikSpecial = SpecialAttack.NONE;
                            this.verzikTicksUntilAttack = (Integer)adjust_for_enrage.apply(7);
                        }
                    }

                    if (this.verzikFirstEnraged) {
                        this.verzikFirstEnraged = false;
                        if (this.verzikSpecial != SpecialAttack.YELLOWS || this.verzikTicksUntilAttack <= 7) {
                            this.verzikTicksUntilAttack = 5;
                        }
                    }
                }
            }
        }

    }

    Color verzikSpecialWarningColor() {
        Color col = Color.WHITE;
        if (this.verzikPhase != Phase.PHASE3) {
            return col;
        } else {
            switch (this.verzikAttackCount) {
                case 4:
                    col = Color.MAGENTA;
                    break;
                case 9:
                    col = Color.ORANGE;
                    break;
                case 14:
                    col = Color.YELLOW;
                    break;
                case 19:
                    col = Color.GREEN;
            }

            return col;
        }
    }

    private void verzikSpawn(NPC npc) {
        this.isHM = VERZIK_HM_ID.contains(npc.getId());
        this.verzikActive = true;
        this.verzikNPC = npc;
        this.verzikSpecial = SpecialAttack.NONE;
        this.verzikFirstEnraged = false;
        this.verzikEnraged = false;
        this.verzikRedPhase = false;
        this.verzikTickPaused = true;
        this.verzikAttackCount = 0;
        this.verzikTicksUntilAttack = 0;
        this.verzikTotalTicksUntilAttack = 0;
        this.verzikLastAnimation = -1;
        this.verzikYellows = 0;
        this.verzikHardmodeSeenYellows = false;
        this.verzikLightningAttacks = 4;
    }

    private void verzikCleanup() {
        this.isHM = false;
        this.verzikNPC = null;
        this.verzikPhase = null;
        this.verzikSpecial = SpecialAttack.NONE;
        this.verzikAggros.clear();
        this.verzikReds.clear();
        this.verzikTornadoes.clear();
        this.verzikPoisonTiles.clear();
        this.verzikLocalTornado = null;
        this.verzikEnraged = false;
        this.verzikFirstEnraged = false;
        this.verzikRedPhase = false;
        this.verzikActive = false;
        this.verzikTickPaused = true;
        this.verzikTotalTicksUntilAttack = 0;
        this.verzikLastAnimation = -1;
        this.verzikYellows = 0;
        this.verzikHardmodeSeenYellows = false;
        this.verzikRangedAttacks.clear();
        this.verzikLightningAttacks = 4;
        this.upcomingAttackQueue.clear();
    }

    public boolean isVerzikActive() {
        return this.verzikActive;
    }

    public NPC getVerzikNPC() {
        return this.verzikNPC;
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

    public Phase getVerzikPhase() {
        return this.verzikPhase;
    }

    public SpecialAttack getVerzikSpecial() {
        return this.verzikSpecial;
    }

    public int getVerzikYellows() {
        return this.verzikYellows;
    }

    public Map<Projectile, WorldPoint> getVerzikRangeProjectiles() {
        return this.verzikRangeProjectiles;
    }

    public HashSet<NPC> getVerzikAggros() {
        return this.verzikAggros;
    }

    public Map<NPC, Pair<Integer, Integer>> getVerzikReds() {
        return this.verzikReds;
    }

    public NPC getVerzikLocalTornado() {
        return this.verzikLocalTornado;
    }

    public HashSet<MemorizedTornado> getVerzikTornadoes() {
        return this.verzikTornadoes;
    }

    public int getVerzikLightningAttacks() {
        return this.verzikLightningAttacks;
    }

    public int getVerzikTicksUntilAttack() {
        return this.verzikTicksUntilAttack;
    }

    public int getVerzikTotalTicksUntilAttack() {
        return this.verzikTotalTicksUntilAttack;
    }

    public int getVerzikAttackCount() {
        return this.verzikAttackCount;
    }

    long getLastTick() {
        return this.lastTick;
    }

    Queue<TheatreUpcomingAttack> getUpcomingAttackQueue() {
        return this.upcomingAttackQueue;
    }

    Set<VerzikPoisonTile> getVerzikPoisonTiles() {
        return this.verzikPoisonTiles;
    }

    public boolean isVerzikEnraged() {
        return this.verzikEnraged;
    }

    public boolean isHM() {
        return this.isHM;
    }

    static enum SpecialAttack {
        WEB_COOLDOWN,
        WEBS,
        YELLOWS,
        GREEN,
        NONE;

        private SpecialAttack() {
        }
    }

    static enum Phase {
        PHASE1,
        PHASE2,
        PHASE3;

        private Phase() {
        }
    }
}
