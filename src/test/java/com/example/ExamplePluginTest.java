package com.example;

import com.example.PrayerFlicker.PrayerFlickerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PacketUtilsPlugin.class,PrayerFlickerPlugin.class);
		RuneLite.main(args);
	}
}