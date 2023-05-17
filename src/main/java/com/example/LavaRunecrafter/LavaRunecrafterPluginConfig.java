package com.example.LavaRunecrafter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("LavaRunecrafter")
public interface LavaRunecrafterPluginConfig extends Config {
    @ConfigItem(
            keyName = "TeleMethod",
            name = "Teleport Method",
            description = "Which method you will use to get to the altar"
    )
    default TeleportMethods TeleMethod() {
        return TeleportMethods.RING_OF_DUELING;
    }

    @ConfigItem(
            keyName = "VialSmasher",
            name = "Vial Smasher Enabled",
            description = "Does the account have vial smasher enabled?"
    )
    default boolean VialSmasher() {
        return false;
    }
}