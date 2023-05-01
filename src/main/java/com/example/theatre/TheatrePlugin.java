//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.example.theatre.Bloat.Bloat;
import com.example.theatre.Maiden.Maiden;
import com.example.theatre.Nylocas.Nylocas;
import com.example.theatre.Sotetseg.Sotetseg;
import com.example.theatre.Verzik.Verzik;
import com.example.theatre.Xarpus.Xarpus;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@PluginDescriptor(
        name = "Teatro Sangriento",
        description = "All-in-one plugin for Theatre of Blood",
        tags = {"ToB", "Theatre", "raids", "bloat", "verzik", "nylo", "xarpus", "sotetseg", "maiden","pajau"},
        enabledByDefault = false
)
public class TheatrePlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(TheatrePlugin.class);
    @Inject
    private Client client;
    @Inject
    private Maiden maiden;
    @Inject
    private Bloat bloat;
    @Inject
    private Nylocas nylocas;
    @Inject
    private Sotetseg sotetseg;
    @Inject
    private Xarpus xarpus;
    @Inject
    private Verzik verzik;
    public static final Integer MAIDEN_REGION = 12869;
    public static final Integer BLOAT_REGION = 13125;
    public static final Integer NYLOCAS_REGION = 13122;
    public static final Integer SOTETSEG_REGION_OVERWORLD = 13123;
    public static final Integer SOTETSEG_REGION_UNDERWORLD = 13379;
    public static final Integer XARPUS_REGION = 12612;
    public static final Integer VERZIK_REGION = 12611;
    private Room[] rooms = null;
    private boolean tobActive;
    public static int partySize;

    public TheatrePlugin() {
    }

    public void configure(Binder binder) {
        binder.bind(TheatreInputListener.class);
    }

    @Provides
    TheatreConfig getConfig(ConfigManager configManager) {
        return (TheatreConfig)configManager.getConfig(TheatreConfig.class);
    }

    protected void startUp() {
        Room[] var1;
        int var2;
        int var3;
        Room room;
        if (this.rooms == null) {
            this.rooms = new Room[]{this.maiden, this.bloat, this.nylocas, this.sotetseg, this.xarpus, this.verzik};
            var1 = this.rooms;
            var2 = var1.length;

            for(var3 = 0; var3 < var2; ++var3) {
                room = var1[var3];
                room.init();
            }
        }

        var1 = this.rooms;
        var2 = var1.length;

        for(var3 = 0; var3 < var2; ++var3) {
            room = var1[var3];
            room.load();
        }

    }

    protected void shutDown() {
        Room[] var1 = this.rooms;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Room room = var1[var3];
            room.unload();
        }

    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
        this.maiden.onNpcSpawned(npcSpawned);
        this.bloat.onNpcSpawned(npcSpawned);
        this.nylocas.onNpcSpawned(npcSpawned);
        this.sotetseg.onNpcSpawned(npcSpawned);
        this.xarpus.onNpcSpawned(npcSpawned);
        this.verzik.onNpcSpawned(npcSpawned);
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        this.maiden.onNpcDespawned(npcDespawned);
        this.bloat.onNpcDespawned(npcDespawned);
        this.nylocas.onNpcDespawned(npcDespawned);
        this.sotetseg.onNpcDespawned(npcDespawned);
        this.xarpus.onNpcDespawned(npcDespawned);
        this.verzik.onNpcDespawned(npcDespawned);
    }

    @Subscribe
    public void onNpcChanged(NpcChanged npcChanged) {
        this.nylocas.onNpcChanged(npcChanged);
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.tobActive) {
            partySize = 0;

            for(int i = 330; i < 335; ++i) {
                if (this.client.getVarcStrValue(i) != null && !this.client.getVarcStrValue(i).equals("")) {
                    ++partySize;
                }
            }
        }

        this.maiden.onGameTick(event);
        this.bloat.onGameTick(event);
        this.nylocas.onGameTick(event);
        this.sotetseg.onGameTick(event);
        this.xarpus.onGameTick(event);
        this.verzik.onGameTick(event);
    }

    @Subscribe
    public void onClientTick(ClientTick event) {
        this.nylocas.onClientTick(event);
        this.xarpus.onClientTick(event);
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        this.tobActive = this.client.getVarbitValue(6440) > 1;
        this.bloat.onVarbitChanged(event);
        this.nylocas.onVarbitChanged(event);
        this.xarpus.onVarbitChanged(event);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        this.bloat.onGameStateChanged(gameStateChanged);
        this.nylocas.onGameStateChanged(gameStateChanged);
        this.xarpus.onGameStateChanged(gameStateChanged);
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded entry) {
        this.nylocas.onMenuEntryAdded(entry);
        this.verzik.onMenuEntryAdded(entry);
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked option) {
        this.nylocas.onMenuOptionClicked(option);
    }

    @Subscribe
    public void onMenuOpened(MenuOpened menu) {
        this.nylocas.onMenuOpened(menu);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged change) {
        if (change.getGroup().equals("Theatre")) {
            this.nylocas.onConfigChanged(change);
            this.verzik.onConfigChanged(change);
        }
    }

    @Subscribe
    public void onGraphicsObjectCreated(GraphicsObjectCreated graphicsObjectC) {
        this.bloat.onGraphicsObjectCreated(graphicsObjectC);
    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event) {
        this.sotetseg.onGroundObjectSpawned(event);
        this.xarpus.onGroundObjectSpawned(event);
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged animationChanged) {
        this.bloat.onAnimationChanged(animationChanged);
        this.nylocas.onAnimationChanged(animationChanged);
        this.sotetseg.onAnimationChanged(animationChanged);
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        this.sotetseg.onProjectileMoved(event);
        this.verzik.onProjectileMoved(event);
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned gameObject) {
        this.verzik.onGameObjectSpawn(gameObject);
    }
}
