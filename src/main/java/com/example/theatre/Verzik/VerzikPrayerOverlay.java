//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Verzik;

import com.example.theatre.TheatreConfig;
import com.example.theatre.prayer.TheatrePrayerOverlay;
import com.example.theatre.prayer.TheatreUpcomingAttack;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.util.Queue;

public class VerzikPrayerOverlay extends TheatrePrayerOverlay {
    private final Verzik plugin;

    @Inject
    protected VerzikPrayerOverlay(Client client, TheatreConfig config, Verzik plugin) {
        super(client, config);
        this.plugin = plugin;
    }

    protected Queue<TheatreUpcomingAttack> getAttackQueue() {
        return this.plugin.getUpcomingAttackQueue();
    }

    protected long getLastTick() {
        return this.plugin.getLastTick();
    }

    protected boolean isEnabled() {
        return this.getConfig().verzikPrayerHelper();
    }
}
