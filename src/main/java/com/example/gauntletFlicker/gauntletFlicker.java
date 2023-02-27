package com.example.gauntletFlicker;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.PacketUtilsPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.NPCPackets;
import com.example.Packets.WidgetPackets;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.HeadIcon;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.Set;

@PluginDescriptor(
		name = "Gauntlet Flicker and Switcher",
		description = "",
		tags = {"ethan"}
)
@PluginDependency(EthanApiPlugin.class)
@PluginDependency(PacketUtilsPlugin.class)
@Slf4j
public class gauntletFlicker extends Plugin
{
	@Inject
	Client client;
	@Inject
	ItemManager manager;
	@Inject
	MousePackets mousePackets;
	@Inject
	WidgetPackets widgetPackets;
	String updatedWeapon = "";
	@Inject
	NPCPackets npcPackets;
	@Inject
	EthanApiPlugin api;
	boolean forceTab = false;
	Set<Integer> HUNLLEF_IDS = Set.of(NpcID.CORRUPTED_HUNLLEF, NpcID.CORRUPTED_HUNLLEF_9036,
			NpcID.CORRUPTED_HUNLLEF_9037, NpcID.CORRUPTED_HUNLLEF_9038, NpcID.CRYSTALLINE_HUNLLEF,
			NpcID.CRYSTALLINE_HUNLLEF_9022, NpcID.CRYSTALLINE_HUNLLEF_9023, NpcID.CRYSTALLINE_HUNLLEF_9024);
	boolean isRange = true;


	@Subscribe
	public void onGameTick(GameTick e)
	{
		if (client.getLocalPlayer().isDead() || client.getLocalPlayer().getHealthRatio() == 0)
		{
			forceTab = false;
			return;
		}
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			forceTab = false;
			return;
		}
		Item weapon = null;
		try
		{
			weapon = client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		}
		catch (NullPointerException ex)
		{
			//todo
		}
		String name = "";
		if (weapon != null)
		{
			ItemComposition itemComp =
					manager.getItemComposition(client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.WEAPON.getSlotIdx()).getId());
			name = itemComp.getName() == null ? "" : itemComp.getName();
		}
		NPC hunllef = client.getNpcs().stream().filter(x -> HUNLLEF_IDS.contains(x.getId())).findFirst().orElse(null);
		if (client.getVarbitValue(9177) != 1)
		{
			forceTab = false;
			return;
		}
		if (client.getVarbitValue(9178) != 1)
		{
			isRange = true;
			forceTab = false;
			updatedWeapon = "";
			return;
		}
		if (forceTab)
		{
			client.runScript(915, 3);
			forceTab = false;
		}
		if (client.getWidget(5046276) == null)
		{
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetAction(client.getWidget(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB), "Setup");
			forceTab = true;
		}
		if (isRange && !api.isQuickPrayerActive(QuickPrayer.PROTECT_FROM_MISSILES))
		{
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetActionPacket(1, 5046276, -1, 13); //quickPrayer range
		}
		else if (!isRange && !api.isQuickPrayerActive(QuickPrayer.PROTECT_FROM_MAGIC))
		{
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetActionPacket(1, 5046276, -1, 12); //quickPrayer magic
		}
		if (hunllef != null)
		{
			if (api.getHeadIcon(hunllef) == HeadIcon.MAGIC && (!name.contains("bow") && !name.contains("halberd")))
			{
				Widget bow = api.getItem("*bow*");
				Widget halberd = api.getItem("*halberd*");
				if (bow != null)
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetAction(bow, "Wield");
					updatedWeapon = "bow";
				}
				else if (halberd != null)
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetAction(halberd, "Wield");
					updatedWeapon = "halberd";
				}
			}
			if (api.getHeadIcon(hunllef) == HeadIcon.RANGED && (!name.contains("staff") && !name.contains("halberd")))
			{
				Widget staff = api.getItem("*staff*");
				Widget halberd = api.getItem("*halberd*");
				if (staff != null)
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetAction(staff, "Wield");
					updatedWeapon = "staff";
				}
				else if (halberd != null)
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetAction(halberd, "Wield");
					updatedWeapon = "halberd";
				}
			}
			if (api.getHeadIcon(hunllef) == HeadIcon.MELEE && (!name.contains("staff") && !name.contains("bow")))
			{
				Widget staff = api.getItem("*staff*");
				Widget bow = api.getItem("*bow*");
				if (staff != null)
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetAction(staff, "Wield");
					updatedWeapon = "staff";
				}
				else if (bow != null)
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetAction(bow, "Wield");
					updatedWeapon = "bow";
				}
			}
			String weaponTesting = updatedWeapon.isEmpty() ? name : updatedWeapon;
			if (weaponTesting.contains("bow"))
			{
				if (rigourUnlocked() && !api.isQuickPrayerActive(QuickPrayer.RIGOUR))
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetActionPacket(1, 5046276, -1, 24); //quickPrayer rigour
				}
				else if (!rigourUnlocked() && !api.isQuickPrayerActive(QuickPrayer.EAGLE_EYE))
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetActionPacket(1, 5046276, -1, 22); //quickPrayer eagle eye
				}
			}
			else if (weaponTesting.contains("staff"))
			{
				if (auguryUnlucked() && !api.isQuickPrayerActive(QuickPrayer.AUGURY))
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetActionPacket(1, 5046276, -1, 27); //quickPrayer augury
				}
				else if (!auguryUnlucked() && !api.isQuickPrayerActive(QuickPrayer.MYSTIC_MIGHT))
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetActionPacket(1, 5046276, -1, 23); //quickPrayer mystic might
				}
			}
			else if (weaponTesting.contains("halberd"))
			{
				if (pietyUnlocked() && !api.isQuickPrayerActive(QuickPrayer.PIETY))
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetActionPacket(1, 5046276, -1, 26); //quickPrayer augury
				}
				else if (!pietyUnlocked() && !api.isQuickPrayerActive(QuickPrayer.ULTIMATE_STRENGTH))
				{
					mousePackets.queueClickPacket();
					widgetPackets.queueWidgetActionPacket(1, 5046276, -1, 10); //quickPrayer mystic might
					if (!api.isQuickPrayerActive(QuickPrayer.INCREDIBLE_REFLEXES))
					{
						mousePackets.queueClickPacket();
						widgetPackets.queueWidgetActionPacket(1, 5046276, -1, 11); //quickPrayer augury
					}
				}
			}
		}
		if (api.isQuickPrayerEnabled())
		{
			api.togglePrayer();
		}
		api.togglePrayer();
	}

	public boolean rigourUnlocked()
	{
		return !(client.getVarbitValue(5451) == 0);
	}

	public boolean pietyUnlocked()
	{
		return client.getVarbitValue(3909) == 8;
	}

	public boolean auguryUnlucked()
	{
		return !(client.getVarbitValue(5452) == 0);
	}


	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuOption().toLowerCase().contains("pass"))
		{
			isRange = true;
		}
	}

	@Override
	protected void startUp()
	{
		isRange = true;
		forceTab = false;
		updatedWeapon = "";
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged e)
	{
		if (e.getActor() == null)
		{
			return;
		}
		if (e.getActor().getAnimation() == 8754)
		{
			isRange = false;
		}
		if (e.getActor().getAnimation() == 8755)
		{
			isRange = true;
		}
	}

	private boolean isHunllefVarbitSet()
	{
		return client.getVarbitValue(9177) == 1;
	}
}