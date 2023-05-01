//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Nylocas;

import com.google.common.collect.ImmutableSet;
import com.example.theatre.*;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.ui.overlay.components.InfoBoxComponent;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ObjectUtils;

import javax.inject.Inject;
import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.*;

public class Nylocas extends Room {
    @Inject
    private Client client;
    @Inject
    private NylocasOverlay nylocasOverlay;
    @Inject
    private NylocasAliveCounterOverlay nylocasAliveCounterOverlay;
    @Inject
    private TheatreInputListener theatreInputListener;
    @Inject
    private MouseManager mouseManager;
    @Inject
    private SkillIconManager skillIconManager;
    private boolean nyloActive;
    private NPC nyloBossNPC;
    private boolean nyloBossAlive;
    private static Runnable wave31Callback = null;
    private static Runnable endOfWavesCallback = null;
    private HashMap<NPC, Integer> nylocasPillars = new HashMap();
    private HashMap<NPC, Integer> nylocasNpcs = new HashMap();
    private HashSet<NPC> aggressiveNylocas = new HashSet();
    private Instant nyloWaveStart;
    private int instanceTimer = 0;
    private boolean isInstanceTimerRunning = false;
    private NyloSelectionManager nyloSelectionManager;
    private int nyloBossAttackTickCount = -1;
    private int nyloBossSwitchTickCount = -1;
    private int nyloBossTotalTickCount = -1;
    private int nyloBossStage = 0;
    private WeaponStyle weaponStyle;
    private HashMap<NyloNPC, NPC> currentWave = new HashMap();
    private final Map<LocalPoint, Integer> splitsMap = new HashMap();
    private final Set<NPC> bigNylos = new HashSet();
    private int varbit6447 = -1;
    private boolean nextInstance = true;
    private int nyloWave = 0;
    private int ticksUntilNextWave = 0;
    private int ticksSinceLastWave = 0;
    private int totalStalledWaves = 0;
    private static final int NPCID_NYLOCAS_PILLAR = 8358;
    private boolean skipTickCheck = false;
    private static final String MAGE_NYLO = "Nylocas Hagios";
    private static final String RANGE_NYLO = "Nylocas Toxobolos";
    private static final String MELEE_NYLO = "Nylocas Ischyros";
    private static final Set<Integer> NYLO_BOSS_MELEE = ImmutableSet.of(10808, 10804, 8355);
    private static final Set<Integer> NYLO_BOSS_MAGE = ImmutableSet.of(10809, 10805, 8356);
    private static final Set<Integer> NYLO_BOSS_RANGE = ImmutableSet.of(10810, 10806, 8357);

    @Inject
    protected Nylocas(TheatrePlugin plugin, TheatreConfig config) {
        super(plugin, config);
    }

    public void init() {
        InfoBoxComponent box = new InfoBoxComponent();
        box.setImage(this.skillIconManager.getSkillImage(Skill.ATTACK));
        NyloSelectionBox nyloMeleeOverlay = new NyloSelectionBox(box);
        nyloMeleeOverlay.setSelected(this.config.getHighlightMeleeNylo());
        box = new InfoBoxComponent();
        box.setImage(this.skillIconManager.getSkillImage(Skill.MAGIC));
        NyloSelectionBox nyloMageOverlay = new NyloSelectionBox(box);
        nyloMageOverlay.setSelected(this.config.getHighlightMageNylo());
        box = new InfoBoxComponent();
        box.setImage(this.skillIconManager.getSkillImage(Skill.RANGED));
        NyloSelectionBox nyloRangeOverlay = new NyloSelectionBox(box);
        nyloRangeOverlay.setSelected(this.config.getHighlightRangeNylo());
        this.nyloSelectionManager = new NyloSelectionManager(nyloMeleeOverlay, nyloMageOverlay, nyloRangeOverlay);
        this.nyloSelectionManager.setHidden(!this.config.nyloHighlightOverlay());
        this.nylocasAliveCounterOverlay.setHidden(!this.config.nyloAlivePanel());
        this.nylocasAliveCounterOverlay.setNyloAlive(0);
        this.nylocasAliveCounterOverlay.setMaxNyloAlive(12);
        this.nyloBossNPC = null;
        this.nyloBossAlive = false;
    }

    private void startupNyloOverlay() {
        this.mouseManager.registerMouseListener(this.theatreInputListener);
        if (this.nyloSelectionManager != null) {
            this.overlayManager.add(this.nyloSelectionManager);
            this.nyloSelectionManager.setHidden(!this.config.nyloHighlightOverlay());
        }

        if (this.nylocasAliveCounterOverlay != null) {
            this.overlayManager.add(this.nylocasAliveCounterOverlay);
            this.nylocasAliveCounterOverlay.setHidden(!this.config.nyloAlivePanel());
        }

    }

    private void shutdownNyloOverlay() {
        this.mouseManager.unregisterMouseListener(this.theatreInputListener);
        if (this.nyloSelectionManager != null) {
            this.overlayManager.remove(this.nyloSelectionManager);
            this.nyloSelectionManager.setHidden(true);
        }

        if (this.nylocasAliveCounterOverlay != null) {
            this.overlayManager.remove(this.nylocasAliveCounterOverlay);
            this.nylocasAliveCounterOverlay.setHidden(true);
        }

    }

    public void load() {
        this.overlayManager.add(this.nylocasOverlay);
        this.weaponStyle = null;
    }

    public void unload() {
        this.overlayManager.remove(this.nylocasOverlay);
        this.shutdownNyloOverlay();
        this.nyloBossNPC = null;
        this.nyloBossAlive = false;
        this.nyloWaveStart = null;
        this.weaponStyle = null;
        this.splitsMap.clear();
        this.bigNylos.clear();
    }

    private void resetNylo() {
        this.nyloBossNPC = null;
        this.nyloBossAlive = false;
        this.nylocasPillars.clear();
        this.nylocasNpcs.clear();
        this.aggressiveNylocas.clear();
        this.setNyloWave(0);
        this.currentWave.clear();
        this.totalStalledWaves = 0;
        this.weaponStyle = null;
        this.splitsMap.clear();
        this.bigNylos.clear();
    }

    private void setNyloWave(int wave) {
        this.nyloWave = wave;
        this.nylocasAliveCounterOverlay.setWave(wave);
        if (wave >= 3) {
            this.isInstanceTimerRunning = false;
        }

        if (wave != 0) {
            this.ticksSinceLastWave = ((NylocasWave)NylocasWave.waves.get(wave)).getWaveDelay();
            this.ticksUntilNextWave = ((NylocasWave)NylocasWave.waves.get(wave)).getWaveDelay();
        }

        if (wave >= 20 && this.nylocasAliveCounterOverlay.getMaxNyloAlive() != 24) {
            this.nylocasAliveCounterOverlay.setMaxNyloAlive(24);
        }

        if (wave < 20 && this.nylocasAliveCounterOverlay.getMaxNyloAlive() != 12) {
            this.nylocasAliveCounterOverlay.setMaxNyloAlive(12);
        }

        if (wave == 31 && wave31Callback != null) {
            wave31Callback.run();
        }

    }

    @Subscribe
    public void onConfigChanged(ConfigChanged change) {
        if (change.getKey().equals("nyloHighlightOverlay")) {
            this.nyloSelectionManager.setHidden(!this.config.nyloHighlightOverlay());
        }

        if (change.getKey().equals("nyloAliveCounter")) {
            this.nylocasAliveCounterOverlay.setHidden(!this.config.nyloAlivePanel());
        }

    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
        NPC npc = npcSpawned.getNpc();
        switch (npc.getId()) {
            case 8342:
            case 8343:
            case 8344:
            case 8345:
            case 8346:
            case 8347:
            case 8348:
            case 8349:
            case 8350:
            case 8351:
            case 8352:
            case 8353:
            case 10774:
            case 10775:
            case 10776:
            case 10777:
            case 10778:
            case 10779:
            case 10780:
            case 10781:
            case 10782:
            case 10783:
            case 10784:
            case 10785:
            case 10791:
            case 10792:
            case 10793:
            case 10794:
            case 10795:
            case 10796:
            case 10797:
            case 10798:
            case 10799:
            case 10800:
            case 10801:
            case 10802:
                if (this.nyloActive) {
                    this.nylocasNpcs.put(npc, 52);
                    this.nylocasAliveCounterOverlay.setNyloAlive(this.nylocasNpcs.size());
                    NyloNPC nyloNPC = this.matchNpc(npc);
                    if (nyloNPC != null) {
                        this.currentWave.put(nyloNPC, npc);
                        if (this.currentWave.size() > 2) {
                            this.matchWave();
                        }
                    }
                }
                break;
            case 8354:
            case 8355:
            case 8356:
            case 8357:
            case 10786:
            case 10787:
            case 10788:
            case 10789:
            case 10807:
            case 10808:
            case 10809:
            case 10810:
                this.nyloBossTotalTickCount = -4;
                this.nyloBossAlive = true;
                this.isInstanceTimerRunning = false;
                this.nyloBossNPC = npc;
                break;
            case 8358:
            case 10790:
            case 10811:
                this.nyloActive = true;
                if (this.nylocasPillars.size() > 3) {
                    this.nylocasPillars.clear();
                }

                if (!this.nylocasPillars.containsKey(npc)) {
                    this.nylocasPillars.put(npc, 100);
                }
        }

        int id = npc.getId();
        switch (id) {
            case 8345:
            case 8346:
            case 8347:
            case 10794:
            case 10795:
            case 10796:
                this.bigNylos.add(npc);
            default:
        }
    }

    private void matchWave() {
        Set<NyloNPC> currentWaveKeySet = this.currentWave.keySet();

        for(int wave = this.nyloWave + 1; wave <= 31; ++wave) {
            boolean matched = true;
            HashSet<NyloNPC> potentialWave = ((NylocasWave)NylocasWave.waves.get(wave)).getWaveData();
            Iterator var5 = potentialWave.iterator();

            NyloNPC nyloNPC;
            while(var5.hasNext()) {
                nyloNPC = (NyloNPC)var5.next();
                if (!currentWaveKeySet.contains(nyloNPC)) {
                    matched = false;
                    break;
                }
            }

            if (matched) {
                this.setNyloWave(wave);
                var5 = potentialWave.iterator();

                while(var5.hasNext()) {
                    nyloNPC = (NyloNPC)var5.next();
                    if (nyloNPC.isAggressive()) {
                        this.aggressiveNylocas.add(this.currentWave.get(nyloNPC));
                    }
                }

                this.currentWave.clear();
                return;
            }
        }

    }

    private NyloNPC matchNpc(NPC npc) {
        WorldPoint p = WorldPoint.fromLocalInstance(this.client, npc.getLocalLocation());
        Point point = new Point(p.getRegionX(), p.getRegionY());
        NylocasSpawnPoint spawnPoint = (NylocasSpawnPoint)NylocasSpawnPoint.getLookupMap().get(point);
        if (spawnPoint == null) {
            return null;
        } else {
            NylocasType nylocasType = (NylocasType)NylocasType.getLookupMap().get(npc.getId());
            return nylocasType == null ? null : new NyloNPC(nylocasType, spawnPoint);
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        switch (npc.getId()) {
            case 8342:
            case 8343:
            case 8344:
            case 8345:
            case 8346:
            case 8347:
            case 8348:
            case 8349:
            case 8350:
            case 8351:
            case 8352:
            case 8353:
            case 10774:
            case 10775:
            case 10776:
            case 10777:
            case 10778:
            case 10779:
            case 10780:
            case 10781:
            case 10782:
            case 10783:
            case 10784:
            case 10785:
            case 10791:
            case 10792:
            case 10793:
            case 10794:
            case 10795:
            case 10796:
            case 10797:
            case 10798:
            case 10799:
            case 10800:
            case 10801:
            case 10802:
                if (this.nylocasNpcs.remove(npc) != null) {
                    this.nylocasAliveCounterOverlay.setNyloAlive(this.nylocasNpcs.size());
                }

                this.aggressiveNylocas.remove(npc);
                if (this.nyloWave == 31 && this.nylocasNpcs.size() == 0 && endOfWavesCallback != null) {
                    endOfWavesCallback.run();
                }
                break;
            case 8354:
            case 8355:
            case 8356:
            case 8357:
            case 10786:
            case 10787:
            case 10788:
            case 10789:
            case 10807:
            case 10808:
            case 10809:
            case 10810:
                this.nyloBossAlive = false;
                this.nyloBossAttackTickCount = -1;
                this.nyloBossSwitchTickCount = -1;
                this.nyloBossTotalTickCount = -1;
                break;
            case 8358:
            case 10790:
            case 10811:
                if (this.nylocasPillars.containsKey(npc)) {
                    this.nylocasPillars.remove(npc);
                }

                if (this.nylocasPillars.size() < 1) {
                    this.nyloWaveStart = null;
                    this.nyloActive = false;
                }
        }

    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        Actor actor = event.getActor();
        if (actor instanceof NPC) {
            switch (((NPC)actor).getId()) {
                case 8355:
                case 8356:
                case 8357:
                case 10787:
                case 10788:
                case 10789:
                case 10808:
                case 10809:
                case 10810:
                    if (event.getActor().getAnimation() == 8004 || event.getActor().getAnimation() == 7999 || event.getActor().getAnimation() == 7989) {
                        this.nyloBossAttackTickCount = 5;
                        ++this.nyloBossStage;
                    }
            }
        }

        if (!this.bigNylos.isEmpty() && event.getActor() instanceof NPC) {
            NPC npc = (NPC)event.getActor();
            if (this.bigNylos.contains(npc)) {
                int anim = npc.getAnimation();
                if (anim == 8005 || anim == 7991 || anim == 7998) {
                    this.splitsMap.putIfAbsent(npc.getLocalLocation(), 6);
                    this.bigNylos.remove(npc);
                }
            }
        }

    }

    @Subscribe
    public void onNpcChanged(NpcChanged npcChanged) {
        int npcId = npcChanged.getNpc().getId();
        switch (npcId) {
            case 8355:
            case 8356:
            case 8357:
            case 10787:
            case 10788:
            case 10789:
            case 10808:
            case 10809:
            case 10810:
                this.nyloBossAttackTickCount = 3;
                this.nyloBossSwitchTickCount = 11;
                this.nyloBossStage = 0;
            default:
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        int[] varps = this.client.getVarps();
        int newVarbit6447 = this.client.getVarbitValue(varps, 6447);
        if (this.inRoomRegion(TheatrePlugin.NYLOCAS_REGION) && newVarbit6447 != 0 && newVarbit6447 != this.varbit6447) {
            this.nyloWaveStart = Instant.now();
            if (this.nylocasAliveCounterOverlay != null) {
                this.nylocasAliveCounterOverlay.setNyloWaveStart(this.nyloWaveStart);
            }
        }

        this.varbit6447 = newVarbit6447;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
            if (this.inRoomRegion(TheatrePlugin.NYLOCAS_REGION)) {
                this.startupNyloOverlay();
            } else {
                if (!this.nyloSelectionManager.isHidden() || !this.nylocasAliveCounterOverlay.isHidden()) {
                    this.shutdownNyloOverlay();
                }

                this.resetNylo();
                this.isInstanceTimerRunning = false;
            }

            this.nextInstance = true;
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.inRoomRegion(TheatrePlugin.NYLOCAS_REGION) && this.nyloActive) {
            if (this.skipTickCheck) {
                this.skipTickCheck = false;
            } else {
                if (this.client.getLocalPlayer() == null || this.client.getLocalPlayer().getPlayerComposition() == null) {
                    return;
                }

                int equippedWeapon = (Integer)ObjectUtils.defaultIfNull(this.client.getLocalPlayer().getPlayerComposition().getEquipmentId(KitType.WEAPON), -1);
                this.weaponStyle = (WeaponStyle)WeaponMap.StyleMap.get(equippedWeapon);
            }

            Iterator<NPC> it = this.nylocasNpcs.keySet().iterator();

            NPC pillar;
            int healthPercent;
            while(it.hasNext()) {
                pillar = (NPC)it.next();
                healthPercent = (Integer)this.nylocasNpcs.get(pillar);
                if (healthPercent < 0) {
                    it.remove();
                } else {
                    this.nylocasNpcs.replace(pillar, healthPercent - 1);
                }
            }

            it = this.nylocasPillars.keySet().iterator();

            while(it.hasNext()) {
                pillar = (NPC)it.next();
                healthPercent = pillar.getHealthRatio();
                if (healthPercent > -1) {
                    this.nylocasPillars.replace(pillar, healthPercent);
                }
            }

            if ((this.instanceTimer + 1) % 4 == 1 && this.nyloWave < 31 && this.ticksSinceLastWave < 2) {
                if (this.config.nyloStallMessage() && this.nylocasAliveCounterOverlay.getNyloAlive() >= this.nylocasAliveCounterOverlay.getMaxNyloAlive()) {
                    ++this.totalStalledWaves;
                    this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Stalled Wave: <col=EF1020>" + this.nyloWave + "/" + 31 + "<col=00> - Time:<col=EF1020> " + this.nylocasAliveCounterOverlay.getFormattedTime() + " <col=00>- Nylos Alive: <col=EF1020>" + this.nylocasAliveCounterOverlay.getNyloAlive() + "/" + this.nylocasAliveCounterOverlay.getMaxNyloAlive() + " <col=00>- Total Stalled Waves: <col=EF1020>" + this.totalStalledWaves, "");
                }

                this.ticksUntilNextWave = 4;
            }

            this.ticksSinceLastWave = Math.max(0, this.ticksSinceLastWave - 1);
            this.ticksUntilNextWave = Math.max(0, this.ticksUntilNextWave - 1);
            if (!this.splitsMap.isEmpty()) {
                this.splitsMap.values().removeIf((value) -> {
                    return value <= 1;
                });
                this.splitsMap.replaceAll((key, value) -> {
                    return value - 1;
                });
            }
        }

        if (this.nyloActive && this.nyloBossAlive) {
            --this.nyloBossAttackTickCount;
            --this.nyloBossSwitchTickCount;
            ++this.nyloBossTotalTickCount;
        }

        this.instanceTimer = (this.instanceTimer + 1) % 4;
    }

    @Subscribe
    public void onClientTick(ClientTick event) {
        List<Player> players = this.client.getPlayers();
        Iterator var3 = players.iterator();

        while(true) {
            Point point;
            do {
                do {
                    do {
                        LocalPoint lp;
                        LocalPoint lp1;
                        do {
                            Player player;
                            do {
                                if (!var3.hasNext()) {
                                    return;
                                }

                                player = (Player)var3.next();
                            } while(player.getWorldLocation() == null);

                            lp = player.getLocalLocation();
                            WorldPoint wp = WorldPoint.fromRegion(player.getWorldLocation().getRegionID(), 5, 33, 0);
                            lp1 = LocalPoint.fromWorld(this.client, wp.getX(), wp.getY());
                        } while(lp1 == null);

                        Point base = new Point(lp1.getSceneX(), lp1.getSceneY());
                        point = new Point(lp.getSceneX() - base.getX(), lp.getSceneY() - base.getY());
                    } while(!this.inRoomRegion(TheatrePlugin.BLOAT_REGION));
                } while(point.getX() != -1);
            } while(point.getY() != -1 && point.getY() != -2 && point.getY() != -3);

            if (this.nextInstance) {
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Nylo instance timer started.", "");
                this.instanceTimer = 3;
                this.isInstanceTimerRunning = true;
                this.nextInstance = false;
            }
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (event.isItemOp() && event.getItemOp() == 2) {
            WeaponStyle newStyle = (WeaponStyle)WeaponMap.StyleMap.get(event.getItemId());
            if (newStyle != null) {
                this.skipTickCheck = true;
                this.weaponStyle = newStyle;
            }
        }

    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded entry) {
        if ((this.nyloActive || this.nyloBossAlive) && this.config.removeNyloBossEntries() && entry.getMenuEntry().getType() == MenuAction.NPC_SECOND_OPTION && this.weaponStyle != null) {
            NPC npc = this.client.getCachedNPCs()[entry.getIdentifier()];
            if (npc != null) {
                int id = npc.getId();
                switch (this.weaponStyle) {
                    case MAGIC:
                        if (NYLO_BOSS_MELEE.contains(id) || NYLO_BOSS_RANGE.contains(id)) {
                            this.removeUnwantedNPCs(NYLO_BOSS_MELEE);
                            this.removeUnwantedNPCs(NYLO_BOSS_RANGE);
                        }
                        break;
                    case MELEE:
                        if (NYLO_BOSS_RANGE.contains(id) || NYLO_BOSS_MAGE.contains(id)) {
                            this.removeUnwantedNPCs(NYLO_BOSS_RANGE);
                            this.removeUnwantedNPCs(NYLO_BOSS_MAGE);
                        }
                        break;
                    case RANGE:
                        if (NYLO_BOSS_MELEE.contains(id) || NYLO_BOSS_MAGE.contains(id)) {
                            this.removeUnwantedNPCs(NYLO_BOSS_MAGE);
                            this.removeUnwantedNPCs(NYLO_BOSS_MELEE);
                        }
                }
            }
        }

        if (this.nyloActive) {
            String target = entry.getTarget();
            if (this.config.removeNyloEntries() && entry.getMenuEntry().getType() == MenuAction.NPC_SECOND_OPTION && this.weaponStyle != null) {
                switch (this.weaponStyle) {
                    case MAGIC:
                        if (target.contains("Nylocas Ischyros") || target.contains("Nylocas Toxobolos")) {
                            this.removeUnwantedNPCs("Nylocas Ischyros");
                            this.removeUnwantedNPCs("Nylocas Toxobolos");
                        }
                        break;
                    case MELEE:
                        if (target.contains("Nylocas Toxobolos") || target.contains("Nylocas Hagios")) {
                            this.removeUnwantedNPCs("Nylocas Toxobolos");
                            this.removeUnwantedNPCs("Nylocas Hagios");
                        }
                        break;
                    case RANGE:
                        if (target.contains("Nylocas Ischyros") || target.contains("Nylocas Hagios")) {
                            this.removeUnwantedNPCs("Nylocas Ischyros");
                            this.removeUnwantedNPCs("Nylocas Hagios");
                        }
                }
            }

            if (this.config.nyloRecolorMenu() && entry.getOption().equals("Attack")) {
                MenuEntry[] entries = this.client.getMenuEntries();
                MenuEntry toEdit = entries[entries.length - 1];
                String strippedTarget = Text.removeTags(target);
                if (strippedTarget.startsWith("Nylocas Hagios")) {
                    toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, Color.CYAN));
                } else if (strippedTarget.startsWith("Nylocas Ischyros")) {
                    toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, new Color(255, 188, 188)));
                } else if (strippedTarget.startsWith("Nylocas Toxobolos")) {
                    toEdit.setTarget(ColorUtil.prependColorTag(strippedTarget, Color.GREEN));
                }

                this.client.setMenuEntries(entries);
            }

        }
    }

    @Subscribe
    public void onMenuOpened(MenuOpened menu) {
        if (this.config.nyloRecolorMenu() && this.nyloActive && this.nyloBossAlive) {
            this.client.setMenuEntries((MenuEntry[])Arrays.stream(menu.getMenuEntries()).filter((s) -> {
                return !s.getOption().equals("Examine");
            }).toArray((x$0) -> {
                return new MenuEntry[x$0];
            }));
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

    private void removeUnwantedNPCs(Set<Integer> ids) {
        MenuEntry[] oldEntries = this.client.getMenuEntries();
        MenuEntry[] newEntries = (MenuEntry[])Arrays.stream(oldEntries).filter((e) -> {
            NPC npc = e.getNpc();
            return npc == null || ids.contains(npc.getId());
        }).toArray((x$0) -> {
            return new MenuEntry[x$0];
        });
        if (oldEntries.length != newEntries.length) {
            this.client.setMenuEntries(newEntries);
        }

    }

    public boolean isNyloActive() {
        return this.nyloActive;
    }

    public NPC getNyloBossNPC() {
        return this.nyloBossNPC;
    }

    public boolean isNyloBossAlive() {
        return this.nyloBossAlive;
    }

    public static void setWave31Callback(Runnable wave31Callback) {
        Nylocas.wave31Callback = wave31Callback;
    }

    public static Runnable getWave31Callback() {
        return wave31Callback;
    }

    public static void setEndOfWavesCallback(Runnable endOfWavesCallback) {
        Nylocas.endOfWavesCallback = endOfWavesCallback;
    }

    public static Runnable getEndOfWavesCallback() {
        return endOfWavesCallback;
    }

    public HashMap<NPC, Integer> getNylocasPillars() {
        return this.nylocasPillars;
    }

    public HashMap<NPC, Integer> getNylocasNpcs() {
        return this.nylocasNpcs;
    }

    public HashSet<NPC> getAggressiveNylocas() {
        return this.aggressiveNylocas;
    }

    public Instant getNyloWaveStart() {
        return this.nyloWaveStart;
    }

    public int getInstanceTimer() {
        return this.instanceTimer;
    }

    public boolean isInstanceTimerRunning() {
        return this.isInstanceTimerRunning;
    }

    public NyloSelectionManager getNyloSelectionManager() {
        return this.nyloSelectionManager;
    }

    public int getNyloBossAttackTickCount() {
        return this.nyloBossAttackTickCount;
    }

    public int getNyloBossSwitchTickCount() {
        return this.nyloBossSwitchTickCount;
    }

    public int getNyloBossTotalTickCount() {
        return this.nyloBossTotalTickCount;
    }

    public int getNyloBossStage() {
        return this.nyloBossStage;
    }

    public Map<LocalPoint, Integer> getSplitsMap() {
        return this.splitsMap;
    }

    public int getNyloWave() {
        return this.nyloWave;
    }

    public int getTicksUntilNextWave() {
        return this.ticksUntilNextWave;
    }
}
