package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.Collections.query.ItemQuery;
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

public class Bank {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static List<Widget> bankItems = new ArrayList<>();
    boolean bankUpdate = true;

    public static ItemQuery search() {
        return new ItemQuery(bankItems.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static boolean isOpen() {
        return client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER) != null && !client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER).isHidden();
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged e) {
        if (e.getContainerId() == 95) {
            if (client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER) == null) {
                Bank.bankItems.clear();
            }
            try {
                Bank.bankItems =
                        Arrays.stream(client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
            } catch (NullPointerException ex) {
                Bank.bankItems.clear();
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.HOPPING || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN || gameStateChanged.getGameState() == GameState.CONNECTION_LOST) {
            Bank.bankItems.clear();
        }
    }
    //	@Subscribe
    //	public void onItemContainerChanged(ItemContainerChanged e) throws ExecutionException
    //	{
    //		switch (e.getContainerId())
    //		{
    //			case 95:
    //				Bank.bankItems.clear();
    //				int counter = 0;
    //				for(Item item: e.getItemContainer().getItems()){
    //					if(item==null){
    //						counter++;
    //						continue;
    //					}
    //					if(item.getId() == -1){
    //						counter++;
    //						continue;
    //					}
    //					if(item.getId() == 6512){
    //						counter++;
    //						continue;
    //					}
    //					ItemComposition tempComp = EthanApiPlugin.itemDefs.get(item.getId());
    //					String withdrawCustom = "Withdraw-" + client.getVarbitValue(3960);
    //					String[] actions = new String[]{"", "Withdraw-1", "Withdraw-5", "Withdraw-10", withdrawCustom, "Withdraw-X", "Withdraw-All", "Withdraw-All-but-1", null, "Examine"};
    //					Bank.bankItems.add(new MinimalItemWidget(counter,
    //							WidgetInfo.BANK_ITEM_CONTAINER.getPackedId(), item.getId(),
    //							tempComp.getName(), item.getQuantity(), actions));
    //					counter++;
    //				}
    //				if (client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER) == null)
    //				{
    //					Bank.bankItems.clear();
    //				}
    //				try
    //				{
    //					Bank.bankItems =
    //							Arrays.stream(client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
    //				}
    //				catch (NullPointerException ex)
    //				{
    //					Bank.bankItems.clear();
    //				}
    //				break;
    //		}
    //	}
}
