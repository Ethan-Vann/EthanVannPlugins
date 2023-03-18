package com.example.EthanApiPlugin;

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
import java.util.stream.Collectors;

public class BankInventory
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	static List<Widget> bankIventoryItems = new ArrayList<>();
	public static ItemQuery search(){
		return new ItemQuery(bankIventoryItems);
	}
	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged e){
		switch(e.getContainerId()){
			case 93:
				if(client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER)==null){
					BankInventory.bankIventoryItems.clear();
					return;
				}
				try
				{
					BankInventory.bankIventoryItems =
							Arrays.stream(client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).filter(x->x.getItemId()!=6512&&x.getItemId()!=-1).collect(Collectors.toList());
					break;
				}catch (NullPointerException err){
					BankInventory.bankIventoryItems.clear();
					break;
				}
		}
	}
	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.HOPPING|| gameStateChanged.getGameState() == GameState.LOGIN_SCREEN||gameStateChanged.getGameState()== GameState.CONNECTION_LOST)
		{
			BankInventory.bankIventoryItems.clear();
		}
	}
}
