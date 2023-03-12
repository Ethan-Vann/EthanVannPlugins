package com.example.EthanApiPlugin;

import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Singleton;
import net.runelite.api.Client;
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
public class Bank
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	static List<Widget> bankItems = new ArrayList<>();
	public static ItemQuery search(){
		return new ItemQuery(bankItems);
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
		switch(e.getContainerId()){
			case 95:
				if(client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER)==null){
					Bank.bankItems.clear();
				}
				try
				{
					Bank.bankItems =
							Arrays.stream(client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).collect(Collectors.toList());
				}
				catch (NullPointerException ex)
				{
					Bank.bankItems.clear();
				}
				break;
		}
	}
}
