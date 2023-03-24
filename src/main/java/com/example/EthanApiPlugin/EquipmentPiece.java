package com.example.EthanApiPlugin;

import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.widgets.Widget;

public class EquipmentPiece
{
	Widget realWidget;
	Widget referenceIdWidget;
	EquipmentInventorySlot type;

	public EquipmentPiece(Widget realWidget, Widget referenceWidget, EquipmentInventorySlot type)
	{
		this.realWidget = realWidget;
		this.referenceIdWidget = referenceWidget;
		this.type = type;
	}
}
