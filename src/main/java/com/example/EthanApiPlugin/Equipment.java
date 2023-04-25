package com.example.EthanApiPlugin;

import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Equipment
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	static List<EquipmentItemWidget> equipment = new ArrayList<>();
	static HashMap<Integer, Integer> equipmentSlotWidgetMapping = new HashMap<>();

	static
	{
		equipmentSlotWidgetMapping.put(0, 15);
		equipmentSlotWidgetMapping.put(1, 16);
		equipmentSlotWidgetMapping.put(2, 17);
		equipmentSlotWidgetMapping.put(3, 18);
		equipmentSlotWidgetMapping.put(4, 19);
		equipmentSlotWidgetMapping.put(5, 20);
		equipmentSlotWidgetMapping.put(7, 21);
		equipmentSlotWidgetMapping.put(9, 22);
		equipmentSlotWidgetMapping.put(10, 23);
		equipmentSlotWidgetMapping.put(12, 24);
		equipmentSlotWidgetMapping.put(13, 25);
	}

	public static EquipmentItemQuery search()
	{
		return new EquipmentItemQuery(equipment);
	}

	@SneakyThrows
	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged e)
	{
		if (e.getContainerId() == InventoryID.EQUIPMENT.getId())
		{
			equipment.clear();
			int i = -1;
			for (Item item : e.getItemContainer().getItems())
			{
				i++;
				if (item == null)
				{
					continue;
				}
				if (item.getId() == 6512 || item.getId() == -1)
				{
					continue;
				}
				Widget w = client.getWidget(WidgetInfo.EQUIPMENT.getGroupId(), equipmentSlotWidgetMapping.get(i));
				if(w==null||w.getActions()==null){
					continue;
				}
				equipment.add(new EquipmentItemWidget(w.getName(),item.getId(),w.getId(),i,w.getActions()));
			}
		}
	}
}

