package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("PacketUtils")
public interface PacketUtilsConfig extends Config
{
	@ConfigItem(
			keyName = "debug",
			name = "debug",
			description = "enable menuaction debug output"
	)
	default boolean debug()
	{
		return false;
	}
}