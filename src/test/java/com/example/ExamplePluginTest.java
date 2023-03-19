package com.example;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.LavaRunecrafter.LavaRunecrafterPlugin;
import com.example.NightmareHelper.NightmareHelperPlugin;
import com.example.PrayerFlicker.EthanPrayerFlickerPlugin;
import com.example.UpkeepPlugin.UpkeepPlugin;
import com.example.bigdrizzleplugin.BigDrizzlePlugin;
import com.example.gauntletFlicker.gauntletFlicker;
import com.example.superglass.SuperGlassMakerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EthanApiPlugin.class,PacketUtilsPlugin.class, EthanPrayerFlickerPlugin.class,
				gauntletFlicker.class,
				SuperGlassMakerPlugin.class, UpkeepPlugin.class, LavaRunecrafterPlugin.class,
				NightmareHelperPlugin.class, BigDrizzlePlugin.class);
		RuneLite.main(args);
	}
}