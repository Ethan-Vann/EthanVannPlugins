package com.example;

import com.example.AutoCalvarion.AutoCalvarion;
import com.example.AutoSpindel.AutoSpindel;
import com.example.AutoTele.AutoTele;
import com.example.E3t4g.e3t4g;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.LavaRunecrafter.LavaRunecrafterPlugin;
import com.example.NightmareHelper.NightmareHelperPlugin;
import com.example.PrayerFlicker.EthanPrayerFlickerPlugin;
import com.example.PvPKeysUITesting.PvPKeys;
import com.example.UpkeepPlugin.UpkeepPlugin;
import com.example.gauntletFlicker.gauntletFlicker;
import com.example.harpoon2ticker.SwordFish2Tick;
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
				NightmareHelperPlugin.class, PvPKeys.class, AutoSpindel.class, SwordFish2Tick.class, AutoCalvarion.class
				, e3t4g.class, AutoTele.class);
		RuneLite.main(args);
	}
}