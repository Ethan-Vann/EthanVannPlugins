package com.example.faldita;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.infobox.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;

//@Extension
@PluginDescriptor(name = "PaJau Falda", enabledByDefault = false, description = "Ayuda al grindeo de la falda(nightmare)", tags = {"pajau", "falda", "overlay", "nightmare"})
@Singleton
public class FaldaPlugin extends Plugin {
	private static final Logger log = LoggerFactory.getLogger(FaldaPlugin.class);

	@Inject
	private Client client;

	@Inject
	private FaldaConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private SpriteManager spriteManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private FaldaOverlay overlay;

	@Inject
	private FaldaPrayerOverlay prayerOverlay;

	@Inject
	private FaldaPrayerInfoBox prayerInfoBox;

	@Inject
	private SanfewInfoBox sanfewInfoBox;

	private static final int NIGHTMARE_HUSK_SPAWN = 8565;

	private static final int NIGHTMARE_PARASITE_TOSS = 8606;

	private static final int NIGHTMARE_CHARGE = 8609;

	private static final int NIGHTMARE_MELEE_ATTACK = 8594;

	private static final int NIGHTMARE_RANGE_ATTACK = 8596;

	private static final int NIGHTMARE_MAGIC_ATTACK = 8595;

	private static final int NIGHTMARE_PRE_MUSHROOM = 37738;

	private static final int NIGHTMARE_MUSHROOM = 37739;

	private static final int NIGHTMARE_SHADOW = 1767;

	private static final LocalPoint MIDDLE_LOCATION = new LocalPoint(6208, 8128);

	private static final Set<LocalPoint> PHOSANIS_MIDDLE_LOCATIONS = (Set<LocalPoint>)ImmutableSet.of(new LocalPoint(6208, 7104), new LocalPoint(7232, 7104));

	private static final List<Integer> INACTIVE_TOTEMS = Arrays.asList(new Integer[] { Integer.valueOf(9435), Integer.valueOf(9438), Integer.valueOf(9441), Integer.valueOf(9444) });

	private static final List<Integer> ACTIVE_TOTEMS = Arrays.asList(new Integer[] { Integer.valueOf(9436), Integer.valueOf(9439), Integer.valueOf(9442), Integer.valueOf(9445) });

	private final Map<Integer, MemorizedTotem> totems = new HashMap<>();

	Map<Integer, MemorizedTotem> getTotems() {
		return this.totems;
	}

	private final Map<LocalPoint, GameObject> spores = new HashMap<>();

	Map<LocalPoint, GameObject> getSpores() {
		return this.spores;
	}

	private final Map<Polygon, Player> huskTarget = new HashMap<>();

	Map<Polygon, Player> getHuskTarget() {
		return this.huskTarget;
	}

	private final Map<Integer, Player> parasiteTargets = new HashMap<>();

	Map<Integer, Player> getParasiteTargets() {
		return this.parasiteTargets;
	}

	private final Set<GraphicsObject> shadows = new HashSet<>();

	Set<GraphicsObject> getShadows() {
		return this.shadows;
	}

	private final Set<NPC> husks = new HashSet<>();

	private final Set<NPC> parasites = new HashSet<>();

	private final Set<NPC> sleepwalkers = new HashSet<>();

	@Nullable
	private FaldaAttack pendingNightmareAttack;

	@Nullable
	private NPC nm;

	private boolean inFight;

	private boolean cursed;

	@Nullable
	FaldaAttack getPendingNightmareAttack() {
		return this.pendingNightmareAttack;
	}

	@Nullable
	NPC getNm() {
		return this.nm;
	}

	boolean isInFight() {
		return this.inFight;
	}

	private int ticksUntilNextAttack = 0;

	private boolean parasite;

	int getTicksUntilNextAttack() {
		return this.ticksUntilNextAttack;
	}

	boolean isParasite() {
		return this.parasite;
	}

	private int ticksUntilParasite = 0;

	int getTicksUntilParasite() {
		return this.ticksUntilParasite;
	}

	private boolean nightmareCharging = false;

	boolean isNightmareCharging() {
		return this.nightmareCharging;
	}

	private boolean shadowsSpawning = false;

	private int shadowsTicks;

	boolean isShadowsSpawning() {
		return this.shadowsSpawning;
	}

	int getShadowsTicks() {
		return this.shadowsTicks;
	}

	private int totemsAlive = 0;

	private boolean flash = false;

	boolean isFlash() {
		return this.flash;
	}

	public void setFlash(boolean flash) {
		this.flash = flash;
	}

	public FaldaPlugin() {
		this.inFight = false;
	}

	@Provides
	FaldaConfig provideConfig(ConfigManager configManager) {
		return (FaldaConfig)configManager.getConfig(FaldaConfig.class);
	}

	protected void startUp() {
		this.overlayManager.add(this.overlay);
		this.overlayManager.add(this.prayerOverlay);
		this.overlayManager.add(this.prayerInfoBox);
		this.overlayManager.add(this.sanfewInfoBox);
		reset();
	}

	protected void shutDown() {
		this.overlayManager.remove(this.overlay);
		this.overlayManager.remove(this.prayerOverlay);
		this.overlayManager.remove(this.prayerInfoBox);
		this.overlayManager.remove(this.sanfewInfoBox);
		reset();
	}

	private void reset() {
		this.inFight = false;
		this.nm = null;
		this.pendingNightmareAttack = null;
		this.nightmareCharging = false;
		this.shadowsSpawning = false;
		this.cursed = false;
		this.flash = false;
		this.parasite = false;
		this.ticksUntilNextAttack = 0;
		this.ticksUntilParasite = 0;
		this.shadowsTicks = 0;
		this.totemsAlive = 0;
		this.totems.clear();
		this.spores.clear();
		this.shadows.clear();
		this.husks.clear();
		this.huskTarget.clear();
		this.parasites.clear();
		this.parasiteTargets.clear();
		this.sleepwalkers.clear();
	}

	//Guarda la posicion y el GameObject cuando salen sporas
	@Subscribe
	private void onGameObjectSpawned(GameObjectSpawned event) {
		if (!this.inFight)
			return;
		GameObject gameObj = event.getGameObject();
		int id = gameObj.getId();
		if (id == 37739 || id == 37738)
			this.spores.put(gameObj.getLocalLocation(), gameObj);
	}

	//Saca del Hash la posicion de las sporas
	@Subscribe
	private void onGameObjectDespawned(GameObjectDespawned event) {
		if (!this.inFight)
			return;
		GameObject gameObj = event.getGameObject();
		int id = gameObj.getId();
		if (id == 37739 || id == 37738)
			this.spores.remove(gameObj.getLocalLocation());
	}

	//Guarda las shadows
	@Subscribe
	public void onGraphicsObjectCreated(GraphicsObjectCreated event) {
		if (!this.inFight)
			return;
		if (event.getGraphicsObject().getId() == 1767) {
			this.shadows.add(event.getGraphicsObject());
			this.shadowsSpawning = true;
			this.shadowsTicks = 5;
			this.ticksUntilNextAttack = 5;
		}
	}

	//Guarda al target de los parasitos o husk
	@Subscribe
	private void onProjectileMoved(ProjectileMoved event) {
		Player targetPlayer;
		if (!this.inFight)
			return;
		Projectile projectile = event.getProjectile();
		switch (projectile.getId()) {
			case 1770:
				targetPlayer = (Player)projectile.getInteracting();
				this.parasiteTargets.putIfAbsent(Integer.valueOf(targetPlayer.getId()), targetPlayer);
				break;
			case 1781:
				targetPlayer = (Player)projectile.getInteracting();
				this.huskTarget.putIfAbsent(targetPlayer.getCanvasTilePoly(), targetPlayer);
				break;
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event) {
		Actor actor = event.getActor();
		if (!(actor instanceof NPC))
			return;
		NPC npc = (NPC)actor;
		//si el actor/npc q cambio animacion es nm o pnm-- resetea, guarda el npc y activa al pelea
		if (this.nm == null && npc.getName() != null && (npc.getName().equalsIgnoreCase("The Nightmare") || npc.getName().equalsIgnoreCase("Phosani's Nightmare"))) {
			reset();
			this.nm = npc;
			this.inFight = true;
		}
		if (!this.inFight || !npc.equals(this.nm))
			return;
		int animationId = npc.getAnimation();
		if (animationId == 8595) {	//animacion de magia
			this.ticksUntilNextAttack = 7;
			this.pendingNightmareAttack = this.cursed ? FaldaAttack.CURSE_MAGIC : FaldaAttack.MAGIC;
		} else if (animationId == 8594) {	//animacion de melee
			this.ticksUntilNextAttack = 7;
			this.pendingNightmareAttack = this.cursed ? FaldaAttack.CURSE_MELEE : FaldaAttack.MELEE;
		} else if (animationId == 8596) { //animacion de ranged
			this.ticksUntilNextAttack = 7;
			this.pendingNightmareAttack = this.cursed ? FaldaAttack.CURSE_RANGE : FaldaAttack.RANGE;
		} else if (animationId == 8609 && ((!isPhosanis(npc.getId()) && !MIDDLE_LOCATION.equals(npc.getLocalLocation())) || (isPhosanis(npc.getId()) && !PHOSANIS_MIDDLE_LOCATIONS.contains(npc.getLocalLocation())))) {
			this.nightmareCharging = true;	//animacion de naruto (cuando ya ser teletransporto
			this.ticksUntilNextAttack = 5;
		}
		if (this.nightmareCharging && animationId != -1 && animationId != 8609) // final de naruto
			this.nightmareCharging = false;
		if (animationId != 8565 && !this.huskTarget.isEmpty())
			this.huskTarget.clear();
		if (animationId == 8606) {
			this.ticksUntilParasite = 27;
			if (this.config.parasitesInfoBox()) {
				Timer parasiteInfoBox = new Timer(16200L, ChronoUnit.MILLIS, (BufferedImage)this.itemManager.getImage(25838), this);
				parasiteInfoBox.setTooltip("Parasites");
				this.infoBoxManager.addInfoBox((InfoBox)parasiteInfoBox);
			}
		}
	}

	@Subscribe
	public void onNpcChanged(NpcChanged event) {
		NPC npc = event.getNpc();
		if (this.totems.containsKey(Integer.valueOf(npc.getIndex())))
			((MemorizedTotem)this.totems.get(Integer.valueOf(npc.getIndex()))).updateCurrentPhase(npc.getId());
		if (INACTIVE_TOTEMS.contains(Integer.valueOf(npc.getId()))) {
			this.totems.putIfAbsent(Integer.valueOf(npc.getIndex()), new MemorizedTotem(npc));
			this.totemsAlive++;
		}
		if (ACTIVE_TOTEMS.contains(Integer.valueOf(npc.getId())))
			this.totemsAlive--;
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event) {
		NPC npc = event.getNpc();
		if (npc.getName() != null && npc.getName().equalsIgnoreCase("parasite"))
			this.parasites.add(npc);
		if (npc.getName() != null && npc.getName().equalsIgnoreCase("husk"))
			this.husks.add(npc);
		if (npc.getName() != null && npc.getName().equalsIgnoreCase("sleepwalker"))
			this.sleepwalkers.add(npc);
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event) {
		NPC npc = event.getNpc();
		if (npc.getName() != null && npc.getName().equalsIgnoreCase("sleepwalker"))
			this.sleepwalkers.remove(npc);
	}

	@Subscribe
	public void onActorDeath(ActorDeath event) {
		if (event.getActor() instanceof NPC && event.getActor().getName() != null) {
			NPC npc = (NPC)event.getActor();
			if (npc.getName() != null && npc.getName().equalsIgnoreCase("parasite"))
				this.parasites.remove(npc);
			if (npc.getName() != null && npc.getName().equalsIgnoreCase("husk"))
				this.husks.remove(npc);
		}
	}

	@Subscribe
	private void onChatMessage(ChatMessage event) {
		if (!this.inFight || this.nm == null || event.getType() != ChatMessageType.GAMEMESSAGE)
			return;
		if (event.getMessage().contains("The Nightmare has impregnated you with a deadly parasite!")) {
			Player localPlayer = this.client.getLocalPlayer();
			if (localPlayer != null)
				this.parasiteTargets.putIfAbsent(Integer.valueOf(localPlayer.getId()), localPlayer);
			this.flash = true;
			this.parasite = true;
			this.ticksUntilParasite = 22;
		}
		if (event.getMessage().toLowerCase().contains("the parasite within you has been weakened") || event.getMessage().toLowerCase().contains("the parasite bursts out of you, fully grown"))
			this.parasite = false;
		if (event.getMessage().toLowerCase().contains("the nightmare has cursed you, shuffling your prayers!"))
			this.cursed = true;
		if (event.getMessage().toLowerCase().contains("you feel the effects of the nightmare's curse wear off."))
			this.cursed = false;
		if (this.config.yawnInfoBox() && event.getMessage().toLowerCase().contains("the nightmare's spores have infected you, making you feel drowsy!")) {
			Timer yawnInfoBox = new Timer(15600L, ChronoUnit.MILLIS, this.spriteManager.getSprite(580, 0), this);
			yawnInfoBox.setTooltip("Yawning");
			this.infoBoxManager.addInfoBox((InfoBox)yawnInfoBox);
		}
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event) {
		GameState gamestate = event.getGameState();
		if (gamestate == GameState.LOADING && this.inFight)
			reset();
	}

	@Subscribe
	private void onGameTick(GameTick event) {
		if (!this.inFight || this.nm == null)
			return;
		if (this.nm.getId() == 378 || this.nm.getId() == 377) //378 = THE_NIGHTMARE , 377 = PHOSANIS_NIGHTMARE
			reset();
		this. ticksUntilNextAttack--;
		if (this.ticksUntilParasite > 0) {
			this.ticksUntilParasite--;
			if (this.ticksUntilParasite == 0)
				this.parasiteTargets.clear();
		}
		if (this.pendingNightmareAttack != null && this.ticksUntilNextAttack <= 3)
			this.pendingNightmareAttack = null;
		if (this.shadowsTicks > 0) {
			this.shadowsTicks--;
			if (this.shadowsTicks == 0) {
				this.shadowsSpawning = false;
				this.shadows.clear();
			}
		}
	}

	//cambiar getMenuAction() por getActionParam0()
	/*
	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event) {
		if (!this.inFight || this.nm == null || event.getMenuAction() != MenuAction.NPC_SECOND_OPTION)
			return;
		String target = Text.removeTags(event.getTarget()).toLowerCase();
		if (((target.contains("the nightmare") || target.contains("phosani's nightmare")) && ((this.config
				.hideAttackNightmareTotems() && this.totemsAlive > 0) || (this.config
				.hideAttackNightmareParasites() && this.parasites.size() > 0) || (this.config
				.hideAttackNightmareHusk() && this.husks.size() > 0) || (this.config
				.hideAttackNightmareSleepwalkers() && this.nm.getId() != 11154 && this.sleepwalkers.size() > 0))) || (this.config
				.hideAttackSleepwalkers() && this.nm.getId() == 11154 && target.contains("sleepwalker")))
			this.client.setMenuOptionCount(this.client.getMenuOptionCount() - 1);
	}
	 */

	private boolean isPhosanis(int id) {
		return ((id >= 9416 && id <= 9424) || (id >= 11153 && id <= 11155));
	}
}
