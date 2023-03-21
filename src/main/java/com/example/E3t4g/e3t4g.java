package com.example.E3t4g;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.EthanApiPlugin.Inventory;
import com.example.InteractionApi.InventoryInteraction;
import com.example.Packets.MousePackets;
import com.example.Packets.ObjectPackets;
import com.example.Packets.WidgetPackets;
import com.example.WidgetInfoExtended;
import com.google.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Optional;

@PluginDescriptor(
		name = "3t4g",
		enabledByDefault = false
)
@PluginDependency(EthanApiPlugin.class)
@PluginDependency(com.example.PacketUtilsPlugin.class)
public class e3t4g extends Plugin
{
	@Inject
	MousePackets mousePackets;
	@Inject
	ObjectPackets objectPackets;
	@Inject
	Client client;
	@Inject
	WidgetPackets widgetPackets;
	int[][] rockPos = new int[][]{{3165, 2908}, {3165, 2909}, {3165, 2910}, {3167, 2911}};
	int timeout=0;
	int rock = 0;
	@Inject
	EthanApiPlugin api;
	@Subscribe
	public void onGameTick(GameTick e)
	{
		timeout = timeout == 0 ? 2 : timeout-1;
		if(timeout!=2) return;
		int sizeEmpty = Inventory.search().withId(ItemID.WATERSKIN0).result().size();
		int sizeFilled = Inventory.search().nameContains("Waterskin").result().size();
		if(sizeEmpty>0){
			if(sizeEmpty==sizeFilled){
				MousePackets.queueClickPacket();
				WidgetPackets.queueWidgetAction(client.getWidget(WidgetInfoExtended.SPELL_HUMIDIFY.getPackedId()),"Cast");
				timeout = 10;
				return;
			}
		}
		Optional<Widget> guam = Inventory.search().withId(ItemID.GUAM_LEAF).first();
		Optional<Widget> tar = Inventory.search().withId(ItemID.SWAMP_TAR).first();
		if(guam.isEmpty() || tar.isEmpty()){
			api.stopPlugin(this);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "please make sure you have guam leaf and swamp tar" +
					" before starting",	null);
			return;
		}
		MousePackets.queueClickPacket();
		WidgetPackets.queueWidgetOnWidget(guam.get(), tar.get());
		rock = rock == 3 ? 0 : rock + 1;
		Inventory.search().nameContains("Granite").first().ifPresent(item -> {
			InventoryInteraction.useItem(item, "Drop");
		});
		MousePackets.queueClickPacket();
		ObjectPackets.queueObjectAction(1, 11387, rockPos[rock][0], rockPos[rock][1], false);
	}
}