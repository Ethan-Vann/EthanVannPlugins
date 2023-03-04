//package com.example.E3t4g;
//
//import com.example.EthanApiPlugin.EthanApiPlugin;
//import com.example.Packets.MousePackets;
//import com.example.Packets.ObjectPackets;
//import com.example.Packets.WidgetPackets;
//import com.example.WidgetInfoExtended;
//import com.google.inject.Inject;
//import net.runelite.api.Client;
//import net.runelite.api.ItemID;
//import net.runelite.api.events.GameTick;
//import net.runelite.api.widgets.Widget;
//import net.runelite.api.widgets.WidgetInfo;
//import net.runelite.client.eventbus.Subscribe;
//import net.runelite.client.plugins.Plugin;
//import net.runelite.client.plugins.PluginDependency;
//import net.runelite.client.plugins.PluginDescriptor;
//
//@PluginDescriptor(
//		name = "3t4g",
//		enabledByDefault = false
//)
//@PluginDependency(EthanApiPlugin.class)
//@PluginDependency(com.example.PacketUtilsPlugin.class)
//public class e3t4g extends Plugin
//{
//	@Inject
//	MousePackets mousePackets;
//	@Inject
//	ObjectPackets objectPackets;
//	@Inject
//	Client client;
//	@Inject
//	WidgetPackets widgetPackets;
//	int[][] rockPos = new int[][]{{3165, 2908}, {3165, 2909}, {3165, 2910}, {3165, 2911}};
//	int timeout;
//	int rock = 0;
//	@Inject
//	EthanApiPlugin api;
//	@Subscribe
//	public void onGameTick(GameTick e)
//	{
//		Widget guam = api.getItem(ItemID.GUAM_LEAF, WidgetInfo.INVENTORY);
//		Widget tar = api.getItem(ItemID.SWAMP_TAR, WidgetInfo.INVENTORY);
//		if (guam == null || tar == null)
//		{
//			api.stopPlugin(this);
//			return;
//		}
//		if(api.countItem("Waterskin(0)",WidgetInfo.INVENTORY)==api.countItem("Waterskin",WidgetInfo.INVENTORY)){
//			if(api.countItem("Waterskin(0)",WidgetInfo.INVENTORY)==0){
//				api.stopPlugin(this);
//				return;
//			}
//			mousePackets.queueClickPacket();
//			widgetPackets.queueWidgetAction(client.getWidget(WidgetInfoExtended.SPELL_HUMIDIFY.getPackedId()),"Cast");
//			timeout = 10;
//			return;
//		}
//		timeout = timeout == 0 ? 3 : timeout-1;
//		if(timeout!=3) return;
//		mousePackets.queueClickPacket();
//		widgetPackets.queueWidgetOnWidget(guam, tar);
//		rock = rock == 3 ? 0 : rock + 1;
//		Widget granite = api.getItem("granite");
//		if (granite != null)
//		{
//			mousePackets.queueClickPacket();
//			widgetPackets.queueWidgetAction(granite, "Drop");
//		}
//		mousePackets.queueClickPacket();
//		objectPackets.queueObjectAction(1, 11387, rockPos[rock][1], rockPos[rock][2], false);
//	}
//}