package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.Collections.query.ItemQuery;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BankInventory {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static List<Widget> bankIventoryItems = new ArrayList<>();

    public static ItemQuery search() {
        return new ItemQuery(bankIventoryItems.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    //	@Subscribe
    //	public void onItemContainerChanged(ItemContainerChanged e)
    //	{
    //		switch (e.getContainerId())
    //		{
    //			case 93:
    //				BankInventory.bankIventoryItems.clear();
    //				int counter = 0;
    //				for (Item item : e.getItemContainer().getItems())
    //				{
    //					try
    //					{
    //						if(item==null){
    //							counter++;
    //							continue;
    //						}
    //						if (item.getId() == -1)
    //						{
    //							counter++;
    //							continue;
    //						}
    //						if (item.getId() == 6512)
    //						{
    //							counter++;
    //							continue;
    //						}
    //						ItemComposition tempComp = EthanApiPlugin.itemDefs.get(item.getId());
    //						String withdrawCustom = "Deposit-" + client.getVarbitValue(3960);
    //						String defaultAction = StaticItemInfo.defaultActionMap.get(item.getId());
    //						int extraActionIndex = -1;
    //						if (item.getId() == ItemID.SMALL_POUCH)
    //						{
    //							if (EthanApiPlugin.testBit(client.getVarps()[261], 0))
    //							{
    //								defaultAction = "Empty";
    //							}
    //						}
    //						else if (item.getId() == ItemID.MEDIUM_POUCH || item.getId() == ItemID.MEDIUM_POUCH_5511)
    //						{
    //							if (EthanApiPlugin.testBit(client.getVarps()[261], 1))
    //							{
    //								defaultAction = "Empty";
    //							}
    //						}
    //						else if (item.getId() == ItemID.LARGE_POUCH || item.getId() == ItemID.LARGE_POUCH_5513)
    //						{
    //							if (EthanApiPlugin.testBit(client.getVarps()[261], 2))
    //							{
    //								defaultAction = "Empty";
    //							}
    //						}
    //						else if (item.getId() == ItemID.GIANT_POUCH || item.getId() == ItemID.GIANT_POUCH_5515)
    //						{
    //							if (EthanApiPlugin.testBit(client.getVarps()[261], 3))
    //							{
    //								defaultAction = "Empty";
    //							}
    //						}
    //						else if (item.getId() == ItemID.COLOSSAL_POUCH || item.getId() == ItemID.COLOSSAL_POUCH_26786)
    //						{
    //							if (EthanApiPlugin.testBit(client.getVarps()[261], 4))
    //							{
    //								defaultAction = "Empty";
    //							}
    //						}
    //						else if (item.getId() == ItemID.COAL_BAG || item.getId() == ItemID.OPEN_COAL_BAG)
    //						{
    //							if (EthanApiPlugin.testBit(client.getVarps()[262], 5))
    //							{
    //								defaultAction = "Fill";
    //							}
    //						}
    //						else if (EthanApiPlugin.testBit(client.getVarps()[262], counter))
    //						{
    //							Integer tempActionIndex = StaticItemInfo.extraActionIndex.get(item.getId());
    //							if (tempActionIndex != null)
    //							{
    //								//								System.out.println("triggered 1 with param4");
    //								extraActionIndex = tempActionIndex;
    //							}
    //							else
    //							{
    //								//								System.out.println("triggered 1 without param4");
    //								extraActionIndex = 1;
    //							}
    //						}
    //						else if (EthanApiPlugin.testBit(client.getVarps()[263], counter))
    //						{
    //							//							System.out.println("triggered 2");
    //							extraActionIndex = 0;
    //						}
    //						String extraAction;
    //						if (extraActionIndex == -1)
    //						{
    //							//							System.out.println("setting to default action");
    //							extraAction = defaultAction;
    //						}
    //						else
    //						{
    //							//							System.out.println("setting to extra action:"+tempComp.getInventoryActions()[extraActionIndex]);
    //							extraAction = tempComp.getInventoryActions()[extraActionIndex];
    //						}
    //						String[] actions = new String[]{null, "", "Deposit-1", "Deposit-5", "Deposit-10",
    //								withdrawCustom, "Deposit-X", "Deposit-All", extraAction, "Examine"};
    //						if((item.getId()==ItemID.LOOTING_BAG||item.getId()==ItemID.LOOTING_BAG_22586)&&client.getVarcIntValue(5)==1){
    //							actions[0]="View";
    //						}
    //						switch(client.getVarbitValue(6590)){
    //							case 1 :
    //								actions[1] = "Deposit-5";
    //								break;
    //							case 2 :
    //								actions[1] = "Deposit-10";
    //								break;
    //							case 3 :
    //								actions[1] = "Deposit-"+Math.max(1,client.getVarbitValue(3960));
    //								break;
    //							case 4 :
    //								actions[1] = "Deposit-All";
    //								break;
    //							default:
    //								actions[1] = "Deposit-1";
    //								break;
    //						}
    //						if(client.getVarbitValue(6590)!=0){
    //				                actions[2] = "Deposit-1";
    //						}
    //
    //						BankInventory.bankIventoryItems.add(new MinimalItemWidget(counter,
    //								WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId(), item.getId(),
    //								tempComp.getName(), item.getQuantity(), actions));
    //					}
    //					catch (ExecutionException err)
    //					{
    //						// do nothing
    //					}
    //					counter++;
    //				}
    //				//				if (client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER) == null)
    //				//				{
    //				//					BankInventory.bankIventoryItems.clear();
    //				//					return;
    //				//				}
    //				//				try
    //				//				{
    //				//					BankInventory.bankIventoryItems =
    //				//							Arrays.stream(client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
    //				//					break;
    //				//				}
    //				//				catch (NullPointerException err)
    //				//				{
    //				//					BankInventory.bankIventoryItems.clear();
    //				//					break;
    //				//				}
    //		}
    //	}
    @Subscribe
    public void onWidgetLoaded(WidgetLoaded e) {
        if (e.getGroupId() == WidgetID.BANK_INVENTORY_GROUP_ID) {
            try {
                BankInventory.bankIventoryItems =
                        Arrays.stream(client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
            } catch (NullPointerException err) {
                BankInventory.bankIventoryItems.clear();
            }
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged e) {
        if (e.getContainerId() == 93) {
            if (client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER) == null) {
                BankInventory.bankIventoryItems.clear();
                return;
            }
            try {
                BankInventory.bankIventoryItems =
                        Arrays.stream(client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
                return;
            } catch (NullPointerException err) {
                BankInventory.bankIventoryItems.clear();
                return;
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.HOPPING || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN || gameStateChanged.getGameState() == GameState.CONNECTION_LOST) {
            BankInventory.bankIventoryItems.clear();
        }
    }
}
