package com.example;

import com.example.AgroReset.agroReset;
import com.example.GreenDrags.GreenDrags;
import com.example.Neverlogout.neverlogout;
import com.example.pathmarker.PathMarkerPlugin;
import com.example.AggroReset.aggroReset;
import com.example.AutoTele.AutoTele;
import com.example.E3t4g.e3t4g;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.Gorilas.GorilasPlugin;
import com.example.LavaRunecrafter.LavaRunecrafterPlugin;
import com.example.NightmareHelper.NightmareHelperPlugin;
import com.example.PrayerFlicker.EthanPrayerFlickerPlugin;
import com.example.Toacito.ToacitoPlugin;
import com.example.UpkeepPlugin.UpkeepPlugin;
import com.example.faldita.FaldaPlugin;
import com.example.gauntletFlicker.gauntletFlicker;
import com.example.harpoon2ticker.SwordFish2Tick;
import com.example.superglass.SuperGlassMakerPlugin;
import com.example.theatre.TheatrePlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EthanApiPlugin.class, PacketUtilsPlugin.class, EthanPrayerFlickerPlugin.class,
				gauntletFlicker.class,
				SuperGlassMakerPlugin.class, UpkeepPlugin.class, LavaRunecrafterPlugin.class,
				NightmareHelperPlugin.class, SwordFish2Tick.class, e3t4g.class, AutoTele.class,
				GorilasPlugin.class, ToacitoPlugin.class, FaldaPlugin.class, aggroReset.class, neverlogout.class,
				agroReset.class, GreenDrags.class
		);
		RuneLite.main(args);
	}
}