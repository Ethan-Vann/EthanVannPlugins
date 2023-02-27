package com.example;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.LavaRunecrafter.LavaRunecrafterPlugin;
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
		ExternalPluginManager.loadBuiltin(EthanApiPlugin.class,PacketUtilsPlugin.class,PrayerFlickerPlugin.class,
				gauntletFlicker.class,
				SuperGlassMakerPlugin.class, UpkeepPlugin.class, LavaRunecrafterPlugin.class);
		RuneLite.main(args);
	}
}