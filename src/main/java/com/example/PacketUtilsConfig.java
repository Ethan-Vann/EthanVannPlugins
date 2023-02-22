package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ModifierlessKeybind;

import java.awt.event.KeyEvent;

@ConfigGroup("PacketUtils")
public interface PacketUtilsConfig extends Config {
	@ConfigItem(
			keyName = "Toggle",
			name = "Toggle",
			description = ""
	)
	default ModifierlessKeybind toggle() {
		return new ModifierlessKeybind(KeyEvent.VK_Z, 0);
	}
}