package com.example.EthanApiPlugin;

import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Singleton
public class Inventory
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	static List<Widget> inventoryItems = new ArrayList<>();
	public static ItemQuery search(){
		return new ItemQuery(inventoryItems);
	}
	public static boolean useItem(String name,String... actions){
		return search().withName(name).first().flatMap(item -> {
			MousePackets.queueClickPacket();
			WidgetPackets.queueWidgetAction(item,actions);
			return Optional.of(true);
		}).orElse(false);
	}
	public static boolean useItem(int id,String... actions){
		return search().hasId(id).first().flatMap(item -> {
			MousePackets.queueClickPacket();
			WidgetPackets.queueWidgetAction(item,actions);
			return Optional.of(true);
		}).orElse(false);
	}

	public static boolean useItem(Predicate<? super Widget> predicate,String... actions){
		return search().filter(predicate).first().flatMap(item -> {
			MousePackets.queueClickPacket();
			WidgetPackets.queueWidgetAction(item,actions);
			return Optional.of(true);
		}).orElse(false);
	}
	public static boolean useItemIndex(int index,String... actions){
		return search().indexIs(index).first().flatMap(item -> {
			MousePackets.queueClickPacket();
			WidgetPackets.queueWidgetAction(item,actions);
			return Optional.of(true);
		}).orElse(false);
	}
	public static boolean useItem(Widget item,String... actions){
		if(item==null){
			return false;
		}
		MousePackets.queueClickPacket();
		WidgetPackets.queueWidgetAction(item,actions);
		return true;
	}
	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged e){
		client.runScript(6009, 9764864, 28, 1, -1);
		switch(e.getContainerId()){
			case 93:
				Inventory.inventoryItems = Arrays.stream(client.getWidget(WidgetInfo.INVENTORY).getDynamicChildren()).filter(Objects::nonNull).collect(Collectors.toList());
				break;
		}
	}
	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.HOPPING|| gameStateChanged.getGameState() == GameState.LOGIN_SCREEN||gameStateChanged.getGameState()== GameState.CONNECTION_LOST)
		{
			Inventory.inventoryItems.clear();
		}
	}
}
