package com.example;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.PrayerFlicker.EthanPrayerFlickerPlugin;
import com.example.ezclick.EZClickPlugin;
import com.example.tooltips.TooltipsPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static Class[] core = {

	};
	public static Class[] plugins = {
			TooltipsPlugin.class
	};
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(
				//Core
				EthanApiPlugin.class, PacketUtilsPlugin.class,
				//Plugins
				EthanPrayerFlickerPlugin.class, TooltipsPlugin.class, EZClickPlugin.class);
		RuneLite.main(args);
	}
}