//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.ArrayUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public abstract class Room {
    protected final TheatrePlugin plugin;
    protected final TheatreConfig config;
    @Inject
    protected OverlayManager overlayManager;
    @Inject
    private Client client;

    @Inject
    protected Room(TheatrePlugin plugin, TheatreConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void init() {
    }

    public void load() {
    }

    public void unload() {
    }

    public boolean inRoomRegion(Integer roomRegionId) {
        return ArrayUtils.contains(this.client.getMapRegions(), roomRegionId);
    }
}
