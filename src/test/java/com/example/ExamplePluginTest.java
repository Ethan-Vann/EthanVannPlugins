package com.example;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.Hidden.FocusTesting;
import com.example.PacketUtils.PacketUtilsPlugin;
import com.example.PathingTesting.PathingTesting;
import com.example.PrayerFlicker.EthanPrayerFlickerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(PacketUtilsPlugin.class, EthanApiPlugin.class, FocusTesting.class, PathingTesting.class, EthanPrayerFlickerPlugin.class);
        RuneLite.main(args);
    }
}