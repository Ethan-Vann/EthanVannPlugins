package com.example;

import com.example.PrayerFlicker.PrayerFlickerPlugin;
import com.example.UpkeepPlugin.UpkeepPlugin;
import com.example.gauntletFlicker.gauntletFlicker;
import com.example.superglass.SuperGlassMakerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PacketUtilsPlugin.class,PrayerFlickerPlugin.class, gauntletFlicker.class,
				SuperGlassMakerPlugin.class, UpkeepPlugin.class);
		RuneLite.main(args);
	}
}