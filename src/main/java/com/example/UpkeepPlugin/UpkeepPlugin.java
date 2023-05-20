package com.example.UpkeepPlugin;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.EthanApiPlugin.Inventory;
import com.example.InteractionApi.InventoryInteraction;
import com.example.PacketUtils.PacketUtilsPlugin;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@PluginDescriptor(
        name = "Upkeep Plugin",
        description = "",
        enabledByDefault = false,
        tags = {"ethan"}
)
@PluginDependency(PacketUtilsPlugin.class)
@PluginDependency(EthanApiPlugin.class)
@Slf4j
public class UpkeepPlugin extends Plugin {
    public int timeout = 0;
    @Inject
    Client client;
    @Inject
    UpkeepPluginConfig config;


    @Provides
    public UpkeepPluginConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(UpkeepPluginConfig.class);
    }

    @Override
    @SneakyThrows
    public void startUp() {
        timeout = 0;
    }

    @Override
    public void shutDown() {

    }

    @Subscribe
    public void onGameTick(GameTick event) throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        int hitpoints = this.client.getBoostedSkillLevel(Skill.HITPOINTS);
        int prayer = this.client.getBoostedSkillLevel(Skill.PRAYER);
        int magic = this.client.getBoostedSkillLevel(Skill.MAGIC);
        int stamina = client.getEnergy() / 100;
        int ranged = this.client.getBoostedSkillLevel(Skill.RANGED);
        int strength = this.client.getBoostedSkillLevel(Skill.STRENGTH);
        if (hitpoints < config.HealthLowAmount()) {
            handleAction(config.HealthActions());
        }
        if (prayer < config.PrayerLowAmount()) {
            handleAction(config.PrayerActions());
        }
        if (magic < config.MagicLowAmount()) {
            handleAction(config.MagicActions());
        }
        if (stamina < config.StaminaLowAmount()) {
            handleAction(config.StaminaActions());
        }
        if (ranged < config.RangeLowAmount()) {
            handleAction(config.RangeActions());
        }
        if (strength < config.StrengthLowAmount()) {
            handleAction(config.StrengthActions());
        }
        //		if (client.getVarbitValue(102) == 0 && !config.AntiPoisonActions().trim().isEmpty())
        //		{
        //			handleAction(config.AntiPoisonActions());
        //		}
        //		if (client.getVarbitValue(Varbits.ANTIFIRE) == 0 && client.getVarbitValue(Varbits.SUPER_ANTIFIRE) == 0 && !config.AntiFireActions().trim().isEmpty())
        //		{
        //			handleAction(config.AntiFireActions());
        //		}
    }

    public void handleAction(String actionParam) {
        String[] Actions = actionParam.split("\n");
        for (String Action : Actions) {
            if (!Action.contains(":")) {
                continue;
            }
            String action = Action.split(":")[0];
            String itemCSV = Action.split(":")[1];
            String[] items = itemCSV.split(",");
            for (String item : items) {
                Optional<Widget> itemBeingUsed = StringUtils.isNumeric(item) ?
                        Inventory.search().withId(Integer.parseInt(item)).first() :
                        Inventory.search().matchesWildCardNoCase(item).first();
                if (itemBeingUsed.isPresent()) {
                    InventoryInteraction.useItem(itemBeingUsed.get(), action);
                    break;
                }
            }
        }
    }
}
