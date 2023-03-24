package com.example.AutoTele;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("AutoTele")
public interface AutoTeleConfig extends Config
{
	@ConfigItem(
			keyName = "combatrange",
			name = "combat range only",
			description = "When checked you will only teleport from players able to attack you"
	)
	default boolean combatrange()
	{
		return true;
	}

	@ConfigItem(
			keyName = "notify",
			name = "chat notification on enter wilderness?",
			description = "When enabled the plugin will tell you if you brought a teleport item or not when entering " +
					"the wilderness"
	)
	default boolean alert()
	{
		return true;
	}
}
