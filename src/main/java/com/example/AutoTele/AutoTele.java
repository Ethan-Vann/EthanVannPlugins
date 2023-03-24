package com.example.AutoTele;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.EthanApiPlugin.Inventory;
import com.example.InteractionApi.InventoryInteraction;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Optional;
import java.util.Set;


@PluginDescriptor(
		name = "AutoTele",
		enabledByDefault = false,
		tags = {"ethan"}
)
public class AutoTele extends Plugin
{
	@Inject
	Client client;
	int timeout;
	static final int RING_OF_WEALTH = 714;
	static final int SEED_POD = 4544;
	int previousLevel = -1;
	public static boolean teleportedFromSkulledPlayer = false;
	@Inject
	AutoTeleConfig config;
	static final Set<Integer> RING_OF_WEALTH_ITEM_IDS = Set.of(ItemID.RING_OF_WEALTH_1, ItemID.RING_OF_WEALTH_2, ItemID.RING_OF_WEALTH_3, ItemID.RING_OF_WEALTH_4, ItemID.RING_OF_WEALTH_5, ItemID.RING_OF_WEALTH_I1, ItemID.RING_OF_WEALTH_I2, ItemID.RING_OF_WEALTH_I3, ItemID.RING_OF_WEALTH_I4, ItemID.RING_OF_WEALTH_I5);

	@Provides
	public AutoTeleConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoTeleConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (timeout > 0)
		{
			timeout--;
			return;
		}
		Widget wildernessLevel = client.getWidget(WidgetInfo.PVP_WILDERNESS_LEVEL);
		int level = -1;
		if (wildernessLevel != null && !wildernessLevel.getText().equals(""))
		{
			try
			{
				if (wildernessLevel.getText().contains("<br>"))
				{
					String text = wildernessLevel.getText().split("<br>")[0];
					level = Integer.parseInt(text.replaceAll("Level: ", ""));
				}
				else
				{
					level = Integer.parseInt(wildernessLevel.getText().replaceAll("Level: ", ""));
				}
			}
			catch (NumberFormatException ex)
			{
				//ignore
			}
		}
		if (previousLevel != -1 && level == -1)
		{
			previousLevel = -1;
		}
		Item rowEquipment = null;
		if (level > -1)
		{
			if (previousLevel == -1)
			{
				previousLevel = level;
				Optional<Widget> royal_seed_pod = Inventory.search().withId(ItemID.ROYAL_SEED_POD).first();
				Optional<Widget> ring_of_wealth = Inventory.search().nameContains("Ring of wealth (").first();
				try
				{
					rowEquipment =
							client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.RING.getSlotIdx());
				}
				catch (NullPointerException ignored)
				{

				}
				if (config.alert())
				{
					if (royal_seed_pod.isPresent() || ring_of_wealth.isPresent() || (rowEquipment != null && RING_OF_WEALTH_ITEM_IDS.contains(rowEquipment.getId())))
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=7cfc00>Entering wilderness with a teleport item." +
								" " +
								"AutoTele is ready to save you", null);
					}
					else
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ff0000> Entering wilderness without a " +
								"supported " +
								"teleport possible mistake?", null);
					}
				}
			}
			if ((rowEquipment != null && !RING_OF_WEALTH_ITEM_IDS.contains(rowEquipment.getId())))
			{
				rowEquipment = null;
			}
		}
		for (Player player : client.getPlayers())
		{

			int lowRange = client.getLocalPlayer().getCombatLevel() - level;
			int highRange = client.getLocalPlayer().getCombatLevel() + level;
			if (player.equals(client.getLocalPlayer()))
			{
				continue;
			}
			if (player.getCombatLevel() >= lowRange && player.getCombatLevel() <= highRange || !config.combatrange())
			{
				boolean teleported = false;
				Optional<Widget> widget = Inventory.search().withId(ItemID.ROYAL_SEED_POD).first();
				if (widget.isPresent())
				{
					teleported = true;
					InventoryInteraction.useItem(widget.get(), "Commune");
				}
				Optional<Widget> row = Inventory.search().nameContains("Ring of wealth (").first();
				if (row.isPresent() && !teleported)
				{
					teleported = true;
					InventoryInteraction.useItem(row.get(), "Rub");
					WidgetPackets.queueResumePause(14352385, 2);
				}
				if (rowEquipment != null && !teleported)
				{
					teleported = true;
					MousePackets.queueClickPacket();
					WidgetPackets.queueWidgetActionPacket(3, 25362456, -1, -1);
				}
				if (teleported)
				{
					teleportedFromSkulledPlayer = EthanApiPlugin.getSkullIcon(player) != null;
					if (teleportedFromSkulledPlayer)
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Teleported from skulled player", null);
					}
					else
					{
						client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Teleported from non-skulled player", null);
					}
				}
				return;
			}
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged e)
	{
		Widget wildernessLevel = client.getWidget(WidgetInfo.PVP_WILDERNESS_LEVEL);
		int level = -1;
		if (wildernessLevel != null && !wildernessLevel.getText().equals(""))
		{
			try
			{
				if (wildernessLevel.getText().contains("<br>"))
				{
					String text = wildernessLevel.getText().split("<br>")[0];
					level = Integer.parseInt(text.replaceAll("Level: ", ""));
				}
				else
				{
					level = Integer.parseInt(wildernessLevel.getText().replaceAll("Level: ", ""));
				}
			}
			catch (NumberFormatException ex)
			{
				//ignore
			}
		}
		if (level > -1)
		{
			if (e.getActor() == client.getLocalPlayer())
			{
				int animation = client.getLocalPlayer().getAnimation();
				if (animation == SEED_POD || animation == RING_OF_WEALTH)
				{
					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "timeout triggered", null);
					timeout = 10;
				}
			}
		}
	}
}
