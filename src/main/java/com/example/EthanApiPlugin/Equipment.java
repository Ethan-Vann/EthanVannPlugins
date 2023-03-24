package com.example.EthanApiPlugin;

import net.runelite.api.Client;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;

public class Equipment
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	static ArrayList<EquipmentPiece> equipment = new ArrayList<>();

	public static EquipmentQuery search()
	{
		System.out.println("beginning size: " + equipment.size());
		return new EquipmentQuery(equipment);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged e)
	{
		//		if (e.getContainerId() == 94)
		//		{
		//			System.out.println("equipment update");
		//			equipment.clear();
		//			Widget helmet = client.getWidget(387, 15);
		//			if (!helmet.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("helmet");
		//				equipment.add(new EquipmentPiece(helmet, helmet.getDynamicChildren()[1], EquipmentInventorySlot.HEAD));
		//			}
		//			Widget cape = client.getWidget(387, 16);
		//			if (!cape.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("cape");
		//				equipment.add(new EquipmentPiece(cape, cape.getDynamicChildren()[1], EquipmentInventorySlot.CAPE));
		//			}
		//			Widget amulet = client.getWidget(387, 17);
		//			if (!amulet.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("amulet");
		//				equipment.add(new EquipmentPiece(amulet, amulet.getDynamicChildren()[1], EquipmentInventorySlot.AMULET));
		//			}
		//			Widget weapon = client.getWidget(387, 18);
		//			if (!weapon.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("weapon");
		//				equipment.add(new EquipmentPiece(weapon, weapon.getDynamicChildren()[1], EquipmentInventorySlot.WEAPON));
		//			}
		//			Widget chest = client.getWidget(387, 19);
		//			if (!chest.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("chest");
		//				equipment.add(new EquipmentPiece(chest, chest.getDynamicChildren()[1],
		//						EquipmentInventorySlot.BODY));
		//			}
		//			Widget shield = client.getWidget(387, 20);
		//			if (!shield.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("shield");
		//				equipment.add(new EquipmentPiece(shield, shield.getDynamicChildren()[1], EquipmentInventorySlot.SHIELD));
		//			}
		//			Widget legs = client.getWidget(387, 21);
		//			if (!legs.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("legs");
		//				equipment.add(new EquipmentPiece(legs, legs.getDynamicChildren()[1], EquipmentInventorySlot.LEGS));
		//			}
		//			Widget gloves = client.getWidget(387, 22);
		//			if (!gloves.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("gloves");
		//				equipment.add(new EquipmentPiece(gloves, gloves.getDynamicChildren()[1], EquipmentInventorySlot.GLOVES));
		//			}
		//			Widget boots = client.getWidget(387, 23);
		//			if (!boots.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("boots");
		//				equipment.add(new EquipmentPiece(boots, boots.getDynamicChildren()[1], EquipmentInventorySlot.BOOTS));
		//			}
		//			Widget ring = client.getWidget(387, 24);
		//			if (!ring.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("ring");
		//				equipment.add(new EquipmentPiece(ring, ring.getDynamicChildren()[1], EquipmentInventorySlot.RING));
		//			}
		//			Widget ammo = client.getWidget(387, 25);
		//			if (!ammo.getDynamicChildren()[1].isHidden())
		//			{
		//				System.out.println("ammo");
		//				equipment.add(new EquipmentPiece(ammo, ammo.getDynamicChildren()[1], EquipmentInventorySlot.AMMO));
		//			}
		//		}
	}
}

