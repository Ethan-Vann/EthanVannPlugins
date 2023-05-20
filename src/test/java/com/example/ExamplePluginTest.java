package com.example;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.PacketUtils.PacketUtilsPlugin;
import com.example.PrayerFlicker.EthanPrayerFlickerPlugin;
import com.example.ezclick.EZClickPlugin;
import com.example.tooltips.TooltipsPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(
				//Ethan
				EthanApiPlugin.class, PacketUtilsPlugin.class,

				EthanPrayerFlickerPlugin.class,

				//Null
				TooltipsPlugin.class, EZClickPlugin.class);

		RuneLite.main(args);
	}
}