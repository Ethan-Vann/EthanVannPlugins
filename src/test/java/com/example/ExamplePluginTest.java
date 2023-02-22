package com.example;

import com.example.PrayerFlicker.PrayerFlickerPlugin;
import com.example.gauntletFlicker.gauntletFlicker;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PacketUtilsPlugin.class,PrayerFlickerPlugin.class, gauntletFlicker.class);
		RuneLite.main(args);
	}
}